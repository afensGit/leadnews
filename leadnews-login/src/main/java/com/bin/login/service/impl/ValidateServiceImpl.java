package com.bin.login.service.impl;

import com.bin.login.service.ValidateService;
import com.bin.model.user.pojos.ApUser;
import com.bin.utils.common.DESUtils;
import com.bin.utils.common.MD5Utils;
import org.springframework.stereotype.Service;

/**
 * @author huangbin
 */
@Service
public class ValidateServiceImpl implements ValidateService {

    @Override
    public boolean validDES(ApUser user, ApUser db) {
        if(db.getPassword().equals(DESUtils.encode(user.getPassword()))){
            return true;
        }
        return false;
    }

    @Override
    public boolean validMD5(ApUser user, ApUser db) {
        if(db.getPassword().equals(MD5Utils.encode(user.getPassword()))){
            return true;
        }
        return false;
    }

    @Override
    public boolean validMD5WithSalt(ApUser user, ApUser db) {
        if(db.getPassword().equals(MD5Utils.encodeWithSalt(user.getPassword(),db.getSalt()))){
            return true;
        }
        return false;
    }
}