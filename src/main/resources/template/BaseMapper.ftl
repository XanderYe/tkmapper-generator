package ${package}.base;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 通用Mapper
 * @author ${author}
 * @date ${date}
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
