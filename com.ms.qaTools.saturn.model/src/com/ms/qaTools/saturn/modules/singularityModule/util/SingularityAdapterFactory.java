package com.ms.qaTools.saturn.modules.singularityModule.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import com.ms.qaTools.saturn.modules.singularityModule.AbstractExtractAction;
import com.ms.qaTools.saturn.modules.singularityModule.AbstractOperation;
import com.ms.qaTools.saturn.modules.singularityModule.DocumentRoot;
import com.ms.qaTools.saturn.modules.singularityModule.ExtractOperation;
import com.ms.qaTools.saturn.modules.singularityModule.SearchExtractOperation;
import com.ms.qaTools.saturn.modules.singularityModule.SingularityConfiguration;
import com.ms.qaTools.saturn.modules.singularityModule.SingularityPackage;
import com.ms.qaTools.saturn.modules.singularityModule.TestCaseExtractOperation;
import com.ms.qaTools.saturn.modules.singularityModule.TestSuiteExtractOperation;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter <code>createXXX</code>
 * method for each class of the model. <!-- end-user-doc -->
 * 
 * @see com.ms.qaTools.saturn.modules.singularityModule.SingularityPackage
 * @generated
 */
public class SingularityAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected static SingularityPackage modelPackage;

  /**
   * Creates an instance of the adapter factory. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  public SingularityAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = SingularityPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object. <!-- begin-user-doc --> This implementation
   * returns <code>true</code> if the object is either the model's package or is an instance object of the model. <!--
   * end-user-doc -->
   * 
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage) { return true; }
    if (object instanceof EObject) { return ((EObject) object).eClass().getEPackage() == modelPackage; }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  protected SingularitySwitch<Adapter> modelSwitch = new SingularitySwitch<Adapter>()
                                                   {
                                                     @Override
                                                     public Adapter caseAbstractExtractAction(
                                                         AbstractExtractAction object)
                                                     {
                                                       return createAbstractExtractActionAdapter();
                                                     }

                                                     @Override
                                                     public Adapter caseAbstractOperation(AbstractOperation object)
                                                     {
                                                       return createAbstractOperationAdapter();
                                                     }

                                                     @Override
                                                     public Adapter caseDocumentRoot(DocumentRoot object)
                                                     {
                                                       return createDocumentRootAdapter();
                                                     }

                                                     @Override
                                                     public Adapter caseExtractOperation(ExtractOperation object)
                                                     {
                                                       return createExtractOperationAdapter();
                                                     }

                                                     @Override
                                                     public Adapter caseSearchExtractOperation(
                                                         SearchExtractOperation object)
                                                     {
                                                       return createSearchExtractOperationAdapter();
                                                     }

                                                     @Override
                                                     public Adapter caseSingularityConfiguration(
                                                         SingularityConfiguration object)
                                                     {
                                                       return createSingularityConfigurationAdapter();
                                                     }

                                                     @Override
                                                     public Adapter caseTestCaseExtractOperation(
                                                         TestCaseExtractOperation object)
                                                     {
                                                       return createTestCaseExtractOperationAdapter();
                                                     }

                                                     @Override
                                                     public Adapter caseTestSuiteExtractOperation(
                                                         TestSuiteExtractOperation object)
                                                     {
                                                       return createTestSuiteExtractOperationAdapter();
                                                     }

                                                     @Override
                                                     public Adapter defaultCase(EObject object)
                                                     {
                                                       return createEObjectAdapter();
                                                     }
                                                   };

  /**
   * Creates an adapter for the <code>target</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @param target
   *          the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject) target);
  }

  /**
   * Creates a new adapter for an object of class '
   * {@link com.ms.qaTools.saturn.modules.singularityModule.AbstractExtractAction <em>Abstract Extract Action</em>}'.
   * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
   * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
   * 
   * @return the new adapter.
   * @see com.ms.qaTools.saturn.modules.singularityModule.AbstractExtractAction
   * @generated
   */
  public Adapter createAbstractExtractActionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '
   * {@link com.ms.qaTools.saturn.modules.singularityModule.AbstractOperation <em>Abstract Operation</em>}'. <!--
   * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
   * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
   * 
   * @return the new adapter.
   * @see com.ms.qaTools.saturn.modules.singularityModule.AbstractOperation
   * @generated
   */
  public Adapter createAbstractOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ms.qaTools.saturn.modules.singularityModule.DocumentRoot
   * <em>Document Root</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we can easily
   * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
   * 
   * @return the new adapter.
   * @see com.ms.qaTools.saturn.modules.singularityModule.DocumentRoot
   * @generated
   */
  public Adapter createDocumentRootAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '
   * {@link com.ms.qaTools.saturn.modules.singularityModule.ExtractOperation <em>Extract Operation</em>}'. <!--
   * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
   * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
   * 
   * @return the new adapter.
   * @see com.ms.qaTools.saturn.modules.singularityModule.ExtractOperation
   * @generated
   */
  public Adapter createExtractOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '
   * {@link com.ms.qaTools.saturn.modules.singularityModule.SearchExtractOperation <em>Search Extract Operation</em>}'.
   * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
   * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
   * 
   * @return the new adapter.
   * @see com.ms.qaTools.saturn.modules.singularityModule.SearchExtractOperation
   * @generated
   */
  public Adapter createSearchExtractOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '
   * {@link com.ms.qaTools.saturn.modules.singularityModule.SingularityConfiguration <em>Configuration</em>}'. <!--
   * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
   * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
   * 
   * @return the new adapter.
   * @see com.ms.qaTools.saturn.modules.singularityModule.SingularityConfiguration
   * @generated
   */
  public Adapter createSingularityConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '
   * {@link com.ms.qaTools.saturn.modules.singularityModule.TestCaseExtractOperation
   * <em>Test Case Extract Operation</em>}'. <!-- begin-user-doc --> This default implementation returns null so that we
   * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
   * end-user-doc -->
   * 
   * @return the new adapter.
   * @see com.ms.qaTools.saturn.modules.singularityModule.TestCaseExtractOperation
   * @generated
   */
  public Adapter createTestCaseExtractOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '
   * {@link com.ms.qaTools.saturn.modules.singularityModule.TestSuiteExtractOperation
   * <em>Test Suite Extract Operation</em>}'. <!-- begin-user-doc --> This default implementation returns null so that
   * we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
   * end-user-doc -->
   * 
   * @return the new adapter.
   * @see com.ms.qaTools.saturn.modules.singularityModule.TestSuiteExtractOperation
   * @generated
   */
  public Adapter createTestSuiteExtractOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case. <!-- begin-user-doc --> This default implementation returns null. <!--
   * end-user-doc -->
   * 
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} // SingularityAdapterFactory
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
