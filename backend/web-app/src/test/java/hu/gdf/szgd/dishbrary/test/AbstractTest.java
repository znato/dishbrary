package hu.gdf.szgd.dishbrary.test;


import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public abstract class AbstractTest {
	@Rule
	public TestName testName = new TestName();
	@Rule
	public TestWatcher testWatcher = new TestWatcher() {
		@Override
		protected void failed(Throwable e, Description description) {
			System.out.println("Test failed!");
		}

		@Override
		protected void succeeded(Description description) {
			System.out.println("Test OK!");
		}
	};

	@Before
	public void logBefore() {
		System.out.println("************ Executing: " + testName.getMethodName() + " *********************");
	}
}
