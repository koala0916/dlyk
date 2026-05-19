package com.bjpowernode.service.impl;

import com.bjpowernode.constant.Constant;
import com.bjpowernode.entity.TPermission;
import com.bjpowernode.entity.TRole;
import com.bjpowernode.entity.TUser;
import com.bjpowernode.mapper.TPermissionMapper;
import com.bjpowernode.mapper.TRoleMapper;
import com.bjpowernode.mapper.TUserMapper;
import com.bjpowernode.query.BaseQuery;
import com.bjpowernode.query.UserQuery;
import com.bjpowernode.service.UserService;
import com.bjpowernode.util.LoginInfoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    //逆向工程、反向工程（根据数据库表，生成mapper接口、mapper.xml、实体类）

    @Resource
    private TUserMapper tUserMapper;

    @Resource
    private TPermissionMapper tPermissionMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private TRoleMapper tRoleMapper;

    /**
     * 该方法在spring security框架登录的时候被调用
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询数据库，查询页面上传过来的这个用户名是否在数据库中存在，也就是根据该username查询用户对象
        TUser tUser = tUserMapper.selectByLoginAct(username);
        if (tUser == null) {
            throw new UsernameNotFoundException("登录账号不存在");
        }

        //查询用户对应的角色
        List<TRole> tRoles = tRoleMapper.selectByUserId(tUser.getId());
        tUser.setTRoleList(tRoles);


        //查询该用户的权限code列表（一个用户可能有多个权限code）
        List<TPermission> tPermissionList = tPermissionMapper.selectByUserId(tUser.getId());
        //把查询出来的角色放入用户对象中
        tUser.setTPermissionList(tPermissionList);

        //返回该用户对象
        return tUser;
    }

    /**
     * 分页查询用户列表
     *
     * @param current
     * @return
     */
    @Override
    public PageInfo<TUser> getUserByPage(Integer current, com.bjpowernode.query.UserSearchQuery search) {
        //1.设置查询第几页，每页查多少条数据
        PageHelper.startPage(current, Constant.PAGE_SIZE);

        //2.调用mapper查询用户列表
        //分页插件pageHelper会在sql语句发出之前拼接分页的sql语句  limit
        List<TUser> tUserList = tUserMapper.selectByPage(new BaseQuery(), search);
        //3.创建PageInfo对象，封装查询结果
        PageInfo<TUser> pageInfo = new PageInfo<>(tUserList);
        return pageInfo;
    }

    @Override
    public TUser getUserById(Integer id) {
        TUser tUser = tUserMapper.selectById(id);
        return tUser;
    }

    @Override
    public int addUser(UserQuery userQuery) {
        TUser tUser = new TUser();
//        tUser.setLoginAct(userQuery.getLoginAct());
//        tUser.setLoginPwd(userQuery.getLoginPwd());
        //将userQuery中的数据复制给tUser  原理：反射
        BeanUtils.copyProperties(userQuery, tUser);
        // 邮箱选填：空串转为 null，避免 UNIQUE(email) 下多个 '' 冲突，且与库字段 NULL 语义一致
        tUser.setEmail(StringUtils.hasText(userQuery.getEmail()) ? userQuery.getEmail().trim() : null);

        tUser.setCreateTime(new Date());//创建时间
        tUser.setCreateBy(LoginInfoUtil.getCurrentLoginUser().getId());

        //密码加密
        tUser.setLoginPwd(passwordEncoder.encode(userQuery.getLoginPwd()));

        return tUserMapper.insertSelective(tUser);
    }

    @Override
    public int editUser(UserQuery userQuery) {
        TUser tUser = new TUser();
        //将userQuery中的数据复制给tUser  原理：反射
        BeanUtils.copyProperties(userQuery, tUser);
        // 邮箱选填：空串转 null，便于插入与唯一索引；编辑时可清空为 NULL
        tUser.setEmail(StringUtils.hasText(userQuery.getEmail()) ? userQuery.getEmail().trim() : null);

        //判断密码是否为空
        if (StringUtils.hasText(userQuery.getLoginPwd2())) {
            tUser.setLoginPwd(passwordEncoder.encode(userQuery.getLoginPwd2()));
        }

        tUser.setEditTime(new Date());
        tUser.setEditBy(LoginInfoUtil.getCurrentLoginUser().getId());


        return tUserMapper.updateByPrimaryKeySelective(tUser);
    }


    @Override
    public int deleteUser(Integer id) {

        //修改了别的表中的数据，需要事务处理
        return tUserMapper.deleteByPrimaryKey(id);

    }

    /**
     * 批量删除
     * @param idList
     * @return
     */
    @Transactional
    @Override
    public int batchDeleteUser(List<String> idList) {

        return tUserMapper.batchDeleteUser(idList);
    }

    @Override
    public List<TUser> getOwners() {
        return tUserMapper.selectOwners();
    }
}
