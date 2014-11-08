package files.xadisk.jencks;
/**
 * 
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.transaction.TransactionManager;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Maciek
 *
 */
@ContextConfiguration( { "file:src/main/resources/applicationContext.xml"})
@TransactionConfiguration(defaultRollback = true, transactionManager = "txManager")
public class XABeanTestJencksRollbackTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private BeanFileXADisk bean;
	
	@Before
	public void clearStorageDirectory() throws IllegalStateException, NamingException {
		File directory = new File("target/fileStorage");
		directory.mkdirs();
		for(File f : directory.listFiles()) {
			f.delete();
		}
		SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
		TransactionManager transactionManager = applicationContext.getBean("geronimoTransactionManager", TransactionManager.class);
		builder.bind("java:comp/TransactionManager", transactionManager);
		builder.activate();
	}
	
	@Test
	public void testFileStore() throws Exception {
		File f = new File("target/fileStorage/test.txt"); 
		bean.storeFile(f, "Only test");
	}
	
	@AfterTransaction
	public void checkDBAndFileStatus() {
		int numberOfTestObjects = simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM JPA.SimpleEntities WHERE value='test'");
		assertEquals(0, numberOfTestObjects);
		File f = new File("target/fileStorage/test.txt"); 
		assertFalse(f.exists());
		
		
	}
	
}
