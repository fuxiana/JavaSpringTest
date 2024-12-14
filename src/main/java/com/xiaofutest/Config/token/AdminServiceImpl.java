package com.xiaofutest.Config.token;

import com.xiaofutest.model.LoginDTO;
import com.xiaofutest.repository.LoginDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author young
 * @description 针对表【admin】的数据库操作Service实现
 * @createDate 2022-09-05 13:41:54
 */
@Service
@Slf4j
public class AdminServiceImpl {

    public static LoginDTO getMsg(LoginDTO loginDTO){
        LoginDTO admin  = LoginDB.selectByUsernameAndPwd(loginDTO.getUsername(), loginDTO.getPassword());;
        Optional.ofNullable(admin).ifPresent(u->{
            //添加token信息设置到用户实体上
            String token = JwtTokenUtils.getToken(String.valueOf(admin.getId()),loginDTO.getPassword());
            log.info("token的值为：{}",token);
            admin.setToken(token);
        });
        return admin;
    }
}



