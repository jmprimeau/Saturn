package com.ms.qaTools.compare.writer
import com.ms.qaTools.compare.CompareColDef
import com.ms.qaTools.io.rowSource.DatabaseConnection
import com.ms.qaTools.io.rowSource.jdbc.ExecuteSupport
import com.ms.qaTools.io.rowSource.jdbc.FetchSupport
import com.ms.qaTools.io.rowSource.jdbc.ResultSetRowSource
import com.ms.qaTools.io.rowSource.Utils._
import com.ms.qaTools.Logger

case class SybaseDiffSetWriter(dbConnection: DatabaseConnection with ExecuteSupport with FetchSupport, colDefs: Seq[CompareColDef], tblPrefix: String)
extends DataBaseDiffSetWriter(dbConnection, colDefs, tblPrefix) {
  val logger = Logger(this.getClass)
  setupDataBaseEnvironment

  /**
   * Method to create the tables used to insert the info from the DiffSet
   */
  private def createTables {
    create("table", "leftTable (rowId varchar(12) not null primary key, " + orderedColDefs(LEFT).map{_.leftColumnDefinition.name + " varchar(256) null "}.toList.mkString(",") + ")")
    create("table", "rightTable (rowId varchar(12) not null primary key, " + orderedColDefs(RIGHT).map{_.rightColumnDefinition.name + " varchar(256) null "}.toList.mkString(",") + ")")
    create("table", "diffs (diffId varchar(12) not null primary key, leftId varchar(12) null, rightId varchar(12) null, statusId varchar(12) null, reason varchar(256) null)")
    create("index", s"diffs_leftId on ${tblPrefix}diffs (leftId)")
    create("index", s"diffs_rightId on ${tblPrefix}diffs (rightId)")
    create("table", "cellDiffs (cellDiffId varchar(12) not null primary key, diffId varchar(12) null, leftIdx varchar(12) null, rightIdx varchar(12) null, statusId varchar(12) null, reason varchar(256) null)")
    create("index", s"cellDiffs_diffId on ${tblPrefix}cellDiffs (diffId)")
    create("table", "xPathDiffs (xPathDiffId varchar(12) not null primary key, diffId varchar(12) null, leftXPath varchar(256) null, rightXPath varchar(256) null, aliasXPath varchar(256) null, statusId varchar(12) null)")
    create("index", s"xPathDiffs_diffId on ${tblPrefix}xPathDiffs (diffId)")
    create("table", "status (statusId varchar(12) not null primary key, status varchar(256) null)")
  }

  private def create(typ: String, statement: String) = {
    val q = s"create $typ " + tblPrefix + statement
    logger.debug("Create Sybase DiffSet table: " + q, null)
    dbConnection.execute(q)
  }

  /**
   * Method used to drop all the tables associated with the dsCompare process
   */
  private def cleanTables {
    dbConnection.fetch("SELECT name from sysobjects WHERE type='U' AND name IN ('diffs', 'cellDiffs', 'xPathDiffs', 'status', 'leftTable', 'rightTable')").foreach(rs => {
      val dropQuery = "DROP TABLE " + rs.toSeqString.mkString
      logger.debug("Clean Sybase Diffset tables: " + dropQuery)
      dbConnection.execute(dropQuery)
    })

    dbConnection.fetch("SELECT name from sysobjects WHERE type='V' AND name IN ('summary', 'different', 'inLeftOnly', 'inRightOnly', 'explained', 'identical')").foreach(rs => {
      val dropQuery = "DROP VIEW " + rs.toSeqString.mkString
      logger.debug("Clean Sybase Diffset views: " + dropQuery)
      dbConnection.execute(dropQuery)
    })
  }

  /**
   * Method to create the views used in the data compare process
   */
  override def createViews {
    val genericColumns = leftColNames.zipWithIndex.map{case (column, index) =>
      tblPrefix + "leftTable." + column + " AS " + column + "_leftVal," +
        tblPrefix + "rightTable." + column + " AS " + column + "_rightVal," +
        "CASE WHEN (" + tblPrefix + "leftTable." + column + " IS NULL) THEN '3' WHEN (" + tblPrefix + "rightTable." + column + " IS NULL) THEN '2'" +
        " ELSE COALESCE((SELECT statusId FROM " + tblPrefix + "cellDiffs WHERE diffId = diffs.diffId and leftIdx = '" + (index + 1) + "'), '0') END AS " + column + "_diffStatus"
    }.toList.mkString(",")

    val createQueries = Seq(
        // Create view summary
        "CREATE VIEW summary AS SELECT * FROM " +
      "(SELECT COUNT(0) AS leftDsNumRows FROM " + tblPrefix + "leftTable) leftDsNumRows," +
      "(SELECT COUNT(0) AS rightDsNumRows FROM " + tblPrefix + "rightTable) rightDsNumRows," +
      "(SELECT COUNT(statusId) AS different FROM " + tblPrefix + "diffs WHERE statusId = '1') different," +
      "(SELECT COUNT(statusId) AS inLeftOnly FROM " + tblPrefix + "diffs WHERE statusId = '2') inLeftOnly," +
      "(SELECT COUNT(statusId) AS inRightOnly FROM " + tblPrefix + "diffs WHERE statusId = '3') inRightOnly," +
      "(SELECT COUNT(statusId) AS explained FROM " + tblPrefix + "diffs WHERE statusId = '4') explained," +
      "(SELECT COUNT(statusId) AS identical FROM " + tblPrefix + "diffs WHERE statusId = '0') identical," +
      "(SELECT COUNT(statusId) AS validatePass FROM " + tblPrefix + "diffs WHERE statusId = '5') validatePass," +
      "(SELECT COUNT(statusId) AS validateFail FROM " + tblPrefix + "diffs WHERE statusId = '6') validateFail" +
      "",
      // Create view inLeftOnly
      "CREATE VIEW inLeftOnly AS SELECT " + tblPrefix + "leftTable.* " +
      "from " + tblPrefix + "diffs AS diffs JOIN " + tblPrefix + "leftTable ON " +
      "leftTable.rowId = diffs.leftId AND diffs.statusId = '2'",
      // Create view inRightOnly
      "CREATE VIEW inRightOnly AS SELECT " + tblPrefix + "rightTable.* " +
      "from " + tblPrefix + "diffs AS diffs JOIN " + tblPrefix + "rightTable ON " +
      "rightTable.rowId = diffs.rightId AND diffs.statusId = '3'",
      // Create view identical
      "CREATE VIEW identical AS SELECT " + tblPrefix + "leftTable.rowId AS leftRowId," +
      tblPrefix + "rightTable.rowId AS rightRowId," +
      tblPrefix + "diffs.statusId AS statusId," +
      // Create dynamically the name of the columns
      genericColumns +
      " FROM " + tblPrefix + "diffs AS diffs JOIN " + tblPrefix + "leftTable ON leftTable.rowId = diffs.leftId " +
      " JOIN " + tblPrefix + "rightTable ON rightTable.rowId = diffs.rightId AND diffs.statusId = '0'",
      // Create view different
      "CREATE VIEW different AS SELECT " + tblPrefix + "leftTable.rowId AS leftRowId," +
      tblPrefix + "rightTable.rowId AS rightRowId," +
      tblPrefix + "diffs.statusId AS statusId," +
      // Create dynamically the name of the columns
      genericColumns +
      " FROM " + tblPrefix + "diffs AS diffs JOIN " + tblPrefix + "leftTable ON leftTable.rowId = diffs.leftId " +
      " JOIN " + tblPrefix + "rightTable ON rightTable.rowId = diffs.rightId AND diffs.statusId = '1'",
      // Create view explained
      "CREATE VIEW explained AS SELECT " + tblPrefix + "leftTable.rowId AS leftRowId," +
      tblPrefix + "rightTable.rowId AS rightRowId," +
      tblPrefix + "diffs.statusId AS statusId," +
      // Create dynamically the name of the columns
      genericColumns +
      " FROM " + tblPrefix + "diffs AS diffs JOIN " + tblPrefix + "leftTable ON leftTable.rowId = diffs.leftId " +
      " JOIN " + tblPrefix + "rightTable ON rightTable.rowId = diffs.rightId AND diffs.statusId = '4'"
      )

    for(q <- createQueries) {
      logger.debug("Create Sybase DiffSet views: " + q, null)
      dbConnection.execute(q)
    }
  }

  /**
   * Method to set up the environment
   */
  override def setupDataBaseEnvironment {
    cleanTables
    createTables
    createValidStatus
  }
}
/*
COPYRIGHT NOTICE AND DISCLAIMER
Copyright (c) 2009-2016, Contributor

This program is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License, version 3, as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License, version 3 for more details.

You should have received a copy of the GNU Lesser General Public License, version 3, along with this program; if not, see http://www.gnu.org/licenses/ or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
THE FOLLOWING DISCLAIMER APPLIES TO ALL SOFTWARE CODE AND OTHER MATERIALS CONTRIBUTED IN CONNECTION WITH THIS PROGRAM:
THIS SOFTWARE IS LICENSED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE AND ANY WARRANTY OF NON-INFRINGEMENT, ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. THIS SOFTWARE MAY BE REDISTRIBUTED TO OTHERS ONLY BY EFFECTIVELY USING THIS OR ANOTHER EQUIVALENT DISCLAIMER AS WELL AS ANY OTHER LICENSE TERMS THAT MAY APPLY.
*/