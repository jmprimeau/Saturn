package com.ms.qaTools.saturn.modules.sqlModule.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.ms.qaTools.saturn.exception.exception;
import com.ms.qaTools.saturn.modules.sqlModule.FetchQuery;
import com.ms.qaTools.saturn.modules.sqlModule.SqlModulePackage;
import com.ms.qaTools.saturn.types.ResourceDefinition;
import com.ms.qaTools.saturn.values.ComplexValue;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Fetch Query</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.ms.qaTools.saturn.modules.sqlModule.impl.FetchQueryImpl#getException <em>Exception</em>}</li>
 *   <li>{@link com.ms.qaTools.saturn.modules.sqlModule.impl.FetchQueryImpl#getSQL <em>SQL</em>}</li>
 *   <li>{@link com.ms.qaTools.saturn.modules.sqlModule.impl.FetchQueryImpl#getOutput <em>Output</em>}</li>
 *   <li>{@link com.ms.qaTools.saturn.modules.sqlModule.impl.FetchQueryImpl#getParameterFile <em>Parameter File</em>}</li>
 *   <li>{@link com.ms.qaTools.saturn.modules.sqlModule.impl.FetchQueryImpl#isEnabled <em>Enabled</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FetchQueryImpl extends EObjectImpl implements FetchQuery
{
  /**
   * The cached value of the '{@link #getException() <em>Exception</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getException()
   * @generated
   * @ordered
   */
  protected exception            exception;

  /**
   * The cached value of the '{@link #getSQL() <em>SQL</em>}' containment reference.
   * <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * @see #getSQL()
   * @generated
   * @ordered
   */
  protected ComplexValue         sQL;

  /**
   * The cached value of the '{@link #getOutput() <em>Output</em>}' containment reference.
   * <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * @see #getOutput()
   * @generated
   * @ordered
   */
  protected ResourceDefinition   output;

  /**
   * The cached value of the '{@link #getParameterFile() <em>Parameter File</em>}' containment reference. <!--
   * begin-user-doc --> <!-- end-user-doc -->
   * 
   * @see #getParameterFile()
   * @generated
   * @ordered
   */
  protected ResourceDefinition   parameterFile;

  /**
   * The default value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
   * <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * @see #isEnabled()
   * @generated
   * @ordered
   */
  protected static final boolean ENABLED_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
   * <!-- begin-user-doc --> <!--
   * end-user-doc -->
   * @see #isEnabled()
   * @generated
   * @ordered
   */
  protected boolean              enabled          = ENABLED_EDEFAULT;

  /**
   * This is true if the Enabled attribute has been set.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  protected boolean              enabledESet;

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  protected FetchQueryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return SqlModulePackage.Literals.FETCH_QUERY;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public exception getException()
  {
    return exception;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetException(exception newException, NotificationChain msgs)
  {
    exception oldException = exception;
    exception = newException;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlModulePackage.FETCH_QUERY__EXCEPTION, oldException, newException);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void setException(exception newException)
  {
    if (newException != exception)
    {
      NotificationChain msgs = null;
      if (exception != null)
        msgs = ((InternalEObject)exception).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlModulePackage.FETCH_QUERY__EXCEPTION, null, msgs);
      if (newException != null)
        msgs = ((InternalEObject)newException).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlModulePackage.FETCH_QUERY__EXCEPTION, null, msgs);
      msgs = basicSetException(newException, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlModulePackage.FETCH_QUERY__EXCEPTION, newException, newException));
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public ComplexValue getSQL()
  {
    return sQL;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSQL(ComplexValue newSQL, NotificationChain msgs)
  {
    ComplexValue oldSQL = sQL;
    sQL = newSQL;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlModulePackage.FETCH_QUERY__SQL, oldSQL, newSQL);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void setSQL(ComplexValue newSQL)
  {
    if (newSQL != sQL)
    {
      NotificationChain msgs = null;
      if (sQL != null)
        msgs = ((InternalEObject)sQL).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlModulePackage.FETCH_QUERY__SQL, null, msgs);
      if (newSQL != null)
        msgs = ((InternalEObject)newSQL).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlModulePackage.FETCH_QUERY__SQL, null, msgs);
      msgs = basicSetSQL(newSQL, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlModulePackage.FETCH_QUERY__SQL, newSQL, newSQL));
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public ResourceDefinition getOutput()
  {
    return output;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOutput(ResourceDefinition newOutput, NotificationChain msgs)
  {
    ResourceDefinition oldOutput = output;
    output = newOutput;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlModulePackage.FETCH_QUERY__OUTPUT, oldOutput, newOutput);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void setOutput(ResourceDefinition newOutput)
  {
    if (newOutput != output)
    {
      NotificationChain msgs = null;
      if (output != null)
        msgs = ((InternalEObject)output).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlModulePackage.FETCH_QUERY__OUTPUT, null, msgs);
      if (newOutput != null)
        msgs = ((InternalEObject)newOutput).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlModulePackage.FETCH_QUERY__OUTPUT, null, msgs);
      msgs = basicSetOutput(newOutput, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlModulePackage.FETCH_QUERY__OUTPUT, newOutput, newOutput));
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public ResourceDefinition getParameterFile()
  {
    return parameterFile;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetParameterFile(ResourceDefinition newParameterFile, NotificationChain msgs)
  {
    ResourceDefinition oldParameterFile = parameterFile;
    parameterFile = newParameterFile;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlModulePackage.FETCH_QUERY__PARAMETER_FILE, oldParameterFile, newParameterFile);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void setParameterFile(ResourceDefinition newParameterFile)
  {
    if (newParameterFile != parameterFile)
    {
      NotificationChain msgs = null;
      if (parameterFile != null)
        msgs = ((InternalEObject)parameterFile).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlModulePackage.FETCH_QUERY__PARAMETER_FILE, null, msgs);
      if (newParameterFile != null)
        msgs = ((InternalEObject)newParameterFile).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlModulePackage.FETCH_QUERY__PARAMETER_FILE, null, msgs);
      msgs = basicSetParameterFile(newParameterFile, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlModulePackage.FETCH_QUERY__PARAMETER_FILE, newParameterFile, newParameterFile));
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public boolean isEnabled()
  {
    return enabled;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void setEnabled(boolean newEnabled)
  {
    boolean oldEnabled = enabled;
    enabled = newEnabled;
    boolean oldEnabledESet = enabledESet;
    enabledESet = true;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlModulePackage.FETCH_QUERY__ENABLED, oldEnabled, enabled, !oldEnabledESet));
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void unsetEnabled()
  {
    boolean oldEnabled = enabled;
    boolean oldEnabledESet = enabledESet;
    enabled = ENABLED_EDEFAULT;
    enabledESet = false;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.UNSET, SqlModulePackage.FETCH_QUERY__ENABLED, oldEnabled, ENABLED_EDEFAULT, oldEnabledESet));
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public boolean isSetEnabled()
  {
    return enabledESet;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case SqlModulePackage.FETCH_QUERY__EXCEPTION:
        return basicSetException(null, msgs);
      case SqlModulePackage.FETCH_QUERY__SQL:
        return basicSetSQL(null, msgs);
      case SqlModulePackage.FETCH_QUERY__OUTPUT:
        return basicSetOutput(null, msgs);
      case SqlModulePackage.FETCH_QUERY__PARAMETER_FILE:
        return basicSetParameterFile(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case SqlModulePackage.FETCH_QUERY__EXCEPTION:
        return getException();
      case SqlModulePackage.FETCH_QUERY__SQL:
        return getSQL();
      case SqlModulePackage.FETCH_QUERY__OUTPUT:
        return getOutput();
      case SqlModulePackage.FETCH_QUERY__PARAMETER_FILE:
        return getParameterFile();
      case SqlModulePackage.FETCH_QUERY__ENABLED:
        return isEnabled();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case SqlModulePackage.FETCH_QUERY__EXCEPTION:
        setException((exception)newValue);
        return;
      case SqlModulePackage.FETCH_QUERY__SQL:
        setSQL((ComplexValue)newValue);
        return;
      case SqlModulePackage.FETCH_QUERY__OUTPUT:
        setOutput((ResourceDefinition)newValue);
        return;
      case SqlModulePackage.FETCH_QUERY__PARAMETER_FILE:
        setParameterFile((ResourceDefinition)newValue);
        return;
      case SqlModulePackage.FETCH_QUERY__ENABLED:
        setEnabled((Boolean)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case SqlModulePackage.FETCH_QUERY__EXCEPTION:
        setException((exception)null);
        return;
      case SqlModulePackage.FETCH_QUERY__SQL:
        setSQL((ComplexValue)null);
        return;
      case SqlModulePackage.FETCH_QUERY__OUTPUT:
        setOutput((ResourceDefinition)null);
        return;
      case SqlModulePackage.FETCH_QUERY__PARAMETER_FILE:
        setParameterFile((ResourceDefinition)null);
        return;
      case SqlModulePackage.FETCH_QUERY__ENABLED:
        unsetEnabled();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case SqlModulePackage.FETCH_QUERY__EXCEPTION:
        return exception != null;
      case SqlModulePackage.FETCH_QUERY__SQL:
        return sQL != null;
      case SqlModulePackage.FETCH_QUERY__OUTPUT:
        return output != null;
      case SqlModulePackage.FETCH_QUERY__PARAMETER_FILE:
        return parameterFile != null;
      case SqlModulePackage.FETCH_QUERY__ENABLED:
        return isSetEnabled();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (enabled: ");
    if (enabledESet) result.append(enabled); else result.append("<unset>");
    result.append(')');
    return result.toString();
  }

} // FetchQueryImpl
/*
Copyright 2017 Morgan Stanley

Licensed under the GNU Lesser General Public License Version 3 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

https://www.gnu.org/licenses/lgpl-3.0.en.html

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

IN ADDITION, THE FOLLOWING DISCLAIMER APPLIES IN CONNECTION WITH THIS PROGRAM:

THIS SOFTWARE IS LICENSED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE AND ANY
WARRANTY OF NON-INFRINGEMENT, ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
OF SUCH DAMAGE. THIS SOFTWARE MAY BE REDISTRIBUTED TO OTHERS ONLY BY EFFECTIVELY
USING THIS OR ANOTHER EQUIVALENT DISCLAIMER IN ADDITION TO ANY OTHER REQUIRED
LICENSE TERMS.
*/
