package cn.intellif.db.core.business.entity.controller;

import cn.intellif.db.core.business.entity.dao.UserDao;
import cn.intellif.db.core.business.entity.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserDao userDao;

    @RequestMapping("/save")
    public Object save(){
        User user  = new User();
        user.setId(89000L);
        user.setName("test444");
        user.setAge(20);
        user.setTelPhone("10086");
        userDao.save(user);
        return "save";
    }

    @RequestMapping("batchSave")
    public Object batchSave(){
        List<User> userList = new LinkedList<>();
        for(int i=0;i<1000;i++){
            User user = new User();
            user.setAge(20);
            user.setTelPhone("12345678"+i);
            user.setName("yinchong"+i);
            userList.add(user);
        }
        userDao.batchSave(userList);
        return "batchSave";
    }


    @RequestMapping("findOne")
    public Object findOne(Long id){
        return userDao.findOne(id);
    }

    @RequestMapping("delete")
    public Object deleteOne(Long id){
        return userDao.delete(id);
    }

    @RequestMapping("update")
    public Object update(){
        User user = new User();
        user.setId(44L);
        user.setTelPhone("186");
        return userDao.update(user);
    }

    @RequestMapping("update2")
    public Object updateNotNull(){
        User user = new User();
        user.setId(45L);
        user.setTelPhone("196");
        return userDao.updateNotNull(user);
    }


}
