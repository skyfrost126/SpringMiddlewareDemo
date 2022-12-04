package entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "person")
public class Person implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String age;
    private String adress;
    private String introducation;
}
