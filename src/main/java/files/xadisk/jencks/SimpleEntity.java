/**
 * 
 */
package files.xadisk.jencks;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wro50095
 *
 */
@Entity
@Table(schema="JPA", name="SimpleEntities")
public class SimpleEntity {
	@Id
	@GeneratedValue
	private Long id;
	
	private String value;
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
