package com.ibm.wala.core.tests.callGraph;

import java.io.IOException;

import org.junit.Test;

import com.ibm.wala.core.tests.util.TestConstants;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.cha.CHACallGraph;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.functions.Function;

public class CHACallGraphTest {
  
  @Test public void testJava_cup() throws ClassHierarchyException, IllegalArgumentException, CancelException, IOException {
    testCHA(TestConstants.JAVA_CUP, TestConstants.JAVA_CUP_MAIN, CallGraphTestUtil.REGRESSION_EXCLUSIONS);
  }
    
  public static CallGraph testCHA(String scopeFile, final String mainClass, final String exclusionsFile) throws ClassHierarchyException, IllegalArgumentException, CancelException, IOException {
    return testCHA(scopeFile, exclusionsFile, new Function<IClassHierarchy, Iterable<Entrypoint>>() {
        @Override
        public Iterable<Entrypoint> apply(IClassHierarchy cha) {
          return Util.makeMainEntrypoints(cha.getScope(), cha, mainClass);
        }
    });
  }
  
  public static CallGraph testCHA(String scopeFile, 
      String exclusionsFile,
      Function<IClassHierarchy, Iterable<Entrypoint>> makeEntrypoints) 
    throws ClassHierarchyException, IllegalArgumentException, CancelException, IOException
  {
    AnalysisScope scope = CallGraphTestUtil.makeJ2SEAnalysisScope(scopeFile, exclusionsFile);
    IClassHierarchy cha = ClassHierarchy.make(scope);
    
    CHACallGraph CG = new CHACallGraph(cha);
    CG.init(makeEntrypoints.apply(cha));
    
    return CG;
  }
  
  public static void main(String[] args) throws ClassHierarchyException, IllegalArgumentException, CancelException, IOException {
    testCHA(args[0], args.length>1? args[1]: null, "Java60RegressionExclusions.txt");
  }
}
