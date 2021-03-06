package com.ms.qaTools.saturn.exception.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.ms.qaTools.saturn.exception.ExceptionPackage;
import com.ms.qaTools.saturn.exception.QATUnknownCode;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>QAT Unknown Code</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link com.ms.qaTools.saturn.exception.impl.QATUnknownCodeImpl#getEnv <em>Env</em>}</li>
 * <li>{@link com.ms.qaTools.saturn.exception.impl.QATUnknownCodeImpl#getRaw <em>Raw</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class QATUnknownCodeImpl extends TraceableExceptionImpl implements QATUnknownCode
{
  /**
   * The default value of the '{@link #getEnv() <em>Env</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getEnv()
   * @generated
   * @ordered
   */
  protected static final String ENV_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getEnv() <em>Env</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getEnv()
   * @generated
   * @ordered
   */
  protected String              env          = ENV_EDEFAULT;

  /**
   * The default value of the '{@link #getRaw() <em>Raw</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getRaw()
   * @generated
   * @ordered
   */
  protected static final String RAW_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRaw() <em>Raw</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getRaw()
   * @generated
   * @ordered
   */
  protected String              raw          = RAW_EDEFAULT;

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  protected QATUnknownCodeImpl()
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
    return ExceptionPackage.Literals.QAT_UNKNOWN_CODE;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public String getEnv()
  {
    return env;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void setEnv(String newEnv)
  {
    String oldEnv = env;
    env = newEnv;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ExceptionPackage.QAT_UNKNOWN_CODE__ENV, oldEnv, env));
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public String getRaw()
  {
    return raw;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void setRaw(String newRaw)
  {
    String oldRaw = raw;
    raw = newRaw;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ExceptionPackage.QAT_UNKNOWN_CODE__RAW, oldRaw, raw));
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
      case ExceptionPackage.QAT_UNKNOWN_CODE__ENV:
        return getEnv();
      case ExceptionPackage.QAT_UNKNOWN_CODE__RAW:
        return getRaw();
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
      case ExceptionPackage.QAT_UNKNOWN_CODE__ENV:
        setEnv((String)newValue);
        return;
      case ExceptionPackage.QAT_UNKNOWN_CODE__RAW:
        setRaw((String)newValue);
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
      case ExceptionPackage.QAT_UNKNOWN_CODE__ENV:
        setEnv(ENV_EDEFAULT);
        return;
      case ExceptionPackage.QAT_UNKNOWN_CODE__RAW:
        setRaw(RAW_EDEFAULT);
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
      case ExceptionPackage.QAT_UNKNOWN_CODE__ENV:
        return ENV_EDEFAULT == null ? env != null : !ENV_EDEFAULT.equals(env);
      case ExceptionPackage.QAT_UNKNOWN_CODE__RAW:
        return RAW_EDEFAULT == null ? raw != null : !RAW_EDEFAULT.equals(raw);
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
    result.append(" (env: ");
    result.append(env);
    result.append(", raw: ");
    result.append(raw);
    result.append(')');
    return result.toString();
  }

} // QATUnknownCodeImpl
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
