package com.zlion.service;

import com.zlion.model.Uav;
import com.zlion.model.User;
import com.zlion.repository.LocationRepository;
import com.zlion.repository.UavRepository;
import com.zlion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by zzs on 2016/9/6.
 */
@Service
public class AuthService {

    private UserRepository userRepository;
    private LocationRepository locationRepository;
    private UavRepository uavRepository;

    @Autowired
    public AuthService(UserRepository userRepository, LocationRepository locationRepository, UavRepository uavRepository) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.uavRepository = uavRepository;
    }


    @Transactional
    public void registe(HttpSession session, User user) throws Exception{
        userRepository.save(user);
        session.setAttribute("auth", user);
        session.setAttribute("authId", user.getId());
    }

    @Transactional
    public boolean loginValidate(HttpSession session, String username, String password){
        User user = userRepository.findByUsernameAndPwd(username, password);
        if (user == null)
            return false;
        else{
            session.setAttribute("auth", user);
            session.setAttribute("authId", user.getId());
            return true;
        }
    }

    @Transactional
    public List<Uav> getUavs(Long userId){
        List<Uav> uavs = uavRepository.findAllByUserId(userId);
        return uavs;
    }

    public void registeUav(Uav uav){

    }

    @Transactional
    public void addUav(Uav uav){
        uavRepository.save(uav);
    }

    @Transactional
    public boolean updateUav(String uuid, String groupName, String info){
        try{
            uavRepository.updateDataByUuid(uuid, groupName, info);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Long getUavBelong(String uuid){
        Uav uav = uavRepository.findByUuid(uuid);
        return uav.getUser_id();
    }

    public Uav getUavDetail(String uuid){
        Uav uav = uavRepository.findByUuid(uuid);
        return uav;
    }

    @Transactional
    public void updateAuth(User user){
        userRepository.updateDetail(user.getId(),user.getEmail(),user.getAddress(),
                user.getPhone(),user.getGroupName());
    }

    @Transactional
    public void updatePwd(HttpSession session, String newPwd){
        userRepository.updateUserPwd((Long) session.getAttribute("authId"), newPwd);
    }

    @Transactional
    public boolean delUav(Long userId, String uuid) throws Exception{
        Uav uav = uavRepository.findByUuid(uuid);
        if(uav.getUser_id() == userId){
            uavRepository.delete(uav);
            return true;
        }
        else{
            return false;
        }
    }

}
