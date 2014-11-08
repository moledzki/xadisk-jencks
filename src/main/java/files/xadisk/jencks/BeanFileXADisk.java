package files.xadisk.jencks;

import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.IOUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xadisk.additional.XAFileOutputStreamWrapper;
import org.xadisk.bridge.proxies.interfaces.XAFileOutputStream;
import org.xadisk.connector.outbound.XADiskConnection;
import org.xadisk.connector.outbound.XADiskConnectionFactory;

/**
 * @author Maciek
 *
 */
public class BeanFileXADisk {
	private XADiskConnectionFactory factory;

	@PersistenceContext
	private EntityManager em;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void storeFile(File whereToStore, String whatToStore) throws Exception {
		XADiskConnection connection = factory.getConnection();

		if(!connection.fileExists(whereToStore)) {
			connection.createFile(whereToStore, false);
		}
		XAFileOutputStream xaFileOutputStream = connection.createXAFileOutputStream(whereToStore, false);
		XAFileOutputStreamWrapper outputStream = new XAFileOutputStreamWrapper(xaFileOutputStream);
		IOUtils.write(whatToStore, outputStream, "UTF-8");
		IOUtils.closeQuietly(outputStream);
		connection.close();
		SimpleEntity foo = new SimpleEntity();
		foo.setValue("test");
		em.persist(foo);
	}

	public void setFactory(XADiskConnectionFactory factory) {
		this.factory = factory;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	

}
