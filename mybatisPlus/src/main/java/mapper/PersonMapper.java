package mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import entity.Person;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PersonMapper extends BaseMapper<Person> {
}
