package com.ms.qaTools.saturn.resources.semaphoreResource.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.ms.qaTools.saturn.resources.semaphoreResource.SemaphoreProvider;
import com.ms.qaTools.saturn.resources.semaphoreResource.SemaphoreResource;
import com.ms.qaTools.saturn.resources.semaphoreResource.SemaphoreResourcePackage;
import com.ms.qaTools.saturn.types.impl.NamedResourceDefinitionImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Semaphore Resource</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link com.ms.qaTools.saturn.resources.semaphoreResource.impl.SemaphoreResourceImpl#getProvider <em>Provider
 * </em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class SemaphoreResourceImpl extends NamedResourceDefinitionImpl implements SemaphoreResource
{
  /**
   * The cached value of the '{@link #getProvider() <em>Provider</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProvider()
   * @generated
   * @ordered
   */
  protected SemaphoreProvider provider;

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  protected SemaphoreResourceImpl()
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
    return SemaphoreResourcePackage.Literals.SEMAPHORE_RESOURCE;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public SemaphoreProvider getProvider()
  {
    return provider;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetProvider(SemaphoreProvider newProvider, NotificationChain msgs)
  {
    SemaphoreProvider oldProvider = provider;
    provider = newProvider;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SemaphoreResourcePackage.SEMAPHORE_RESOURCE__PROVIDER, oldProvider, newProvider);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
  public void setProvider(SemaphoreProvider newProvider)
  {
    if (newProvider != provider)
    {
      NotificationChain msgs = null;
      if (provider != null)
        msgs = ((InternalEObject)provider).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SemaphoreResourcePackage.SEMAPHORE_RESOURCE__PROVIDER, null, msgs);
      if (newProvider != null)
        msgs = ((InternalEObject)newProvider).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SemaphoreResourcePackage.SEMAPHORE_RESOURCE__PROVIDER, null, msgs);
      msgs = basicSetProvider(newProvider, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SemaphoreResourcePackage.SEMAPHORE_RESOURCE__PROVIDER, newProvider, newProvider));
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
      case SemaphoreResourcePackage.SEMAPHORE_RESOURCE__PROVIDER:
        return basicSetProvider(null, msgs);
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
      case SemaphoreResourcePackage.SEMAPHORE_RESOURCE__PROVIDER:
        return getProvider();
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
      case SemaphoreResourcePackage.SEMAPHORE_RESOURCE__PROVIDER:
        setProvider((SemaphoreProvider)newValue);
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
      case SemaphoreResourcePackage.SEMAPHORE_RESOURCE__PROVIDER:
        setProvider((SemaphoreProvider)null);
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
      case SemaphoreResourcePackage.SEMAPHORE_RESOURCE__PROVIDER:
        return provider != null;
    }
    return super.eIsSet(featureID);
  }

} // SemaphoreResourceImpl
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
