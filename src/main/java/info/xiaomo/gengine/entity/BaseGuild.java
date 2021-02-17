package info.xiaomo.gengine.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

/**
 * 公会
 * <p>直接操作mongodb数据库，不缓存</p>
 * <p>
 * <p>
 * 2017/9/18 0018
 */
@Entity
@Data
public class BaseGuild {

	@Id
	@Indexed
	private long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 帮主ID
	 */
	private long masterId;

	/**
	 * 公会成员
	 */
	@Embedded
	private Set<Long> members = new HashSet<>(50);
	/**
	 * 申请列表
	 */
	@Embedded
	private Set<Long> applies = new HashSet<>();
	/**
	 * 创建时间
	 */
	private Date createTime;


}
