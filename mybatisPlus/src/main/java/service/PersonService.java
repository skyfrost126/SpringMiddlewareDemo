package service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import entity.Person;
import mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonMapper mapper;

    /**
     * 查询
     * @return
     */
    public List<Person> list() {
        QueryWrapper<Person> queryWrapper = new QueryWrapper<Person>();
        // 添加查询条件，模糊查询name字段
        queryWrapper.like("name", "张三");
        // 查询id字段大于1
        queryWrapper.gt("id", 1);
        // 查询age字段等于20
        queryWrapper.eq("age", "20");
        return mapper.selectList(queryWrapper);
    }

    /**
     * 计数
     * @return
     */
    public int count() {
        QueryWrapper<Person> queryWrapper = new QueryWrapper<Person>();
        // 添加age字段小于或等于20查询条件
        queryWrapper.le("age", "20");
        return mapper.selectCount(queryWrapper);
    }

    /**
     * 根据条件更新
     * @param user
     * @param column
     * @param val
     */
    public void changeBy(Person user, String column, Object val) {
        QueryWrapper<Person> userQueryWrapper = new QueryWrapper<Person>();
        userQueryWrapper.eq(column, val);
        int num = mapper.update(user, userQueryWrapper);
    }

    /**
     * 通过id修改
     * @param person
     */
    public void changeUserById(Person person) {
        int num = mapper.updateById(person);
    }

    /**
     * 通过id删除
     * @param person
     * @return
     */
    public int deleteById(Person person) {
        return mapper.deleteById(person.getId());
    }

    /**
     * 新增
     */
    public void inset() {
        mapper.insert(new Person());
    }


}
