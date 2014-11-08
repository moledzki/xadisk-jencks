package files.xadisk.jencks;

/**
 * 
 */

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.naming.NamingException;
import javax.transaction.TransactionManager;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * @author Maciek
 * 
 */
@ContextConfiguration({ "file:src/main/resources/applicationContext.xml" })
@TransactionConfiguration(defaultRollback = false, transactionManager = "txManager")
@DirtiesContext
public class XABeanTestJencksCommitTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private BeanFileXADisk bean;

	@Before
	public void clearStorageDirectory() throws IllegalStateException, NamingException {
		final File directory = new File("target/fileStorage");
		directory.mkdirs();
		for (final File f : directory.listFiles()) {
			f.delete();
		}
		final SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
		final TransactionManager transactionManager = this.applicationContext.getBean("geronimoTransactionManager", TransactionManager.class);
		builder.bind("java:comp/TransactionManager", transactionManager);
		builder.activate();
	}

	@Test
	public void testFileStore() throws Exception {
		final File f = new File("target/fileStorage/test.txt");
		this.bean.storeFile(f, "Only test");
	}

	@AfterTransaction
	public void checkDBAndFileStatus() {
		final int numberOfTestObjects = this.simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM JPA.SimpleEntities WHERE value='test'");
		assertEquals(1, numberOfTestObjects);
		final File f = new File("target/fileStorage/test.txt");
		assert f.exists();

	}

}
