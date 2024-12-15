package com.CloudBike.service.impl;

import com.CloudBike.constant.MessageConstant;
import com.CloudBike.constant.StatusConstant;
import com.CloudBike.context.BaseContext;
import com.CloudBike.dto.RideInfoDTO;
import com.CloudBike.dto.RideInfoPageQuery;
import com.CloudBike.entity.Ride;
import com.CloudBike.entity.RideDetail;
import com.CloudBike.entity.User;
import com.CloudBike.exception.BaseException;
import com.CloudBike.mapper.RideMapper;
import com.CloudBike.result.PageResult;
import com.CloudBike.service.IRideService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 骑行团表 服务实现类
 * </p>
 *
 * @author unique
 * @since 2024-11-20
 */
@Service
@Transactional
public class RideServiceImpl extends ServiceImpl<RideMapper, Ride> implements IRideService {

    private final RideMapper rideMapper;

    public RideServiceImpl(RideMapper rideMapper)
    {
        this.rideMapper = rideMapper;
    }

    /**
     * 申请骑行团
     * @param rideInfoDTO
     */
    @Override
    @Transactional
    public void insert(RideInfoDTO rideInfoDTO)
    {
        // 1、获取活动发起者信息
        Integer userId = BaseContext.getCurrentId();
        User user = Db.lambdaQuery(User.class)
                .eq(userId != null, User::getId, userId)
                .one();

        // 2、检测用户的信用状态
        // 2.1、如果信用状态被冻结，无法发起活动
        if (Objects.equals(user.getCredit(), StatusConstant.DISABLED))
        {
            throw new BaseException(MessageConstant.CREDIT_LIMIT);
        }

        // 3、检测用户在活动当天是否有其他骑行活动
        List<RideDetail> list = Db.lambdaQuery(RideDetail.class)
                .eq(RideDetail::getUserId, userId)
                .list();

        // 3.1、如果当天有其他骑行活动，则无法申请
        if (list != null && !list.isEmpty())
        {
            List<Integer> rideIds = list.stream()
                    .map(RideDetail::getRideId)
                    .toList();
            Set<LocalDate> departureTimes = lambdaQuery()
                    .in(Ride::getId, rideIds)
                    .list()
                    .stream()
                    .map(l ->
                    {
                        return l.getDepartureTime().toLocalDate();
                    })
                    .collect(Collectors.toSet());
            if (departureTimes.contains(rideInfoDTO.getDepartureTime().toLocalDate()))
            {
                throw new BaseException(MessageConstant.BUSY_DAY);
            }
        }

        // 4、如果用户信用状态正常且当天无其他骑行活动，允许申请
        // 4.1、属性拷贝
        Ride ride=new Ride();
        BeanUtils.copyProperties(rideInfoDTO,ride);
        ride.setUserId(userId);

        // 4.2、将图片路径集合转为字符串
        List<String> images = rideInfoDTO.getImages();
        String image=null;
        if (images!=null&&!images.isEmpty())
        {
            image = String.join(",", images);
        }
        ride.setImage(image);

        // 4.3、存储骑行团信息
        save(ride);

        // 5、更改用户的发起骑行次数（+1）
        Integer rideTimes = user.getRideTimes() + 1;
        Db.lambdaUpdate(User.class)
                .eq(User::getId, userId)
                .set(User::getRideTimes, rideTimes)
                .update();

        // 6、在骑行明细中添加用户信息
        RideDetail rideDetail = new RideDetail();
        rideDetail.setRideId(ride.getId());
        rideDetail.setUserId(userId);
        Db.save(rideDetail);
    }

    /**
     * 查询最近的骑行团
     *
     * @param name
     * @return
     */
    @Override
    public List<RideInfoDTO> list(String name)
    {
        // 1、根据条件筛选最近的骑行团活动
        List<Ride> list = lambdaQuery()
                // 1.1、骑行团名称匹配（如有）
                .like(name != null && !name.isEmpty(), Ride::getName, name)
                // 1.2、骑行团状态为”已通过“
                .eq(Ride::getStatus, StatusConstant.PASSED)
                // 1.3、报名名额未满
                .apply("participants < max_people")
                // 1.4、出发时间距现在最少2小时
                .gt(Ride::getDepartureTime, LocalDateTime.now().plusHours(2))
                // 1.5、首先根据已报名人数降序再根据出发时间升序排序
                .orderByDesc(Ride::getParticipants)
                .orderByAsc(Ride::getDepartureTime)
                .list();

        // 2、如果无符合条件的结果，返回提示信息
        if (list == null || list.isEmpty())
        {
            throw new BaseException(MessageConstant.EMPTY_RESULT);
        }

        // 3、将查询信息转化为对应DTOS
        List<RideInfoDTO> rideInfoDTOS = new ArrayList<>();

        // 3.1、获取这些活动的发起者id
        List<Integer> userIds = list.stream()
                .map(Ride::getUserId)
                .toList();

        // 3.2、查询所有活动发起者的username，与用户id组成Map
        Map<Integer, String> usernames = Db.lambdaQuery(User.class)
                .in(User::getId, userIds)
                .list()
                .stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));

        // 3.3、组装DTO结果
        list.stream().forEach(l ->
        {
            // 3.4、属性拷贝
            RideInfoDTO rideInfoDTO = new RideInfoDTO();
            BeanUtils.copyProperties(l, rideInfoDTO);

            // 3.5、将图片路径字符串转换为集合
            String image = l.getImage();
            List<String> images=new ArrayList<>();
            if (image!=null&&!image.isEmpty())
            {
                images = Arrays.asList(image.split(","));
            }
            rideInfoDTO.setImages(images);

            // 3.6、补充属性
            rideInfoDTO.setUsername(usernames.get(l.getUserId()));

            // 3.7、将DTO存入DTOS
            rideInfoDTOS.add(rideInfoDTO);
        });

        // 4、返回结果
        return rideInfoDTOS;
    }

    /**
     * 根据id查询骑行团详情
     *
     * @param id
     * @return
     */
    @Override
    public RideInfoDTO one(Integer id)
    {
        // 1、根据id查询骑行团信息
        Ride ride = getById(id);

        // 2、查询骑行团发起者信息
        Integer userId = ride.getUserId();
        User user = Db.lambdaQuery(User.class)
                .eq(User::getId, userId)
                .one();

        // 3、封装属性
        // 3.1、属性拷贝
        RideInfoDTO rideInfoDTO=new RideInfoDTO();
        BeanUtils.copyProperties(ride, rideInfoDTO);

        // 3.2、属性补充
        rideInfoDTO.setRideTimes(user.getRideTimes());
        rideInfoDTO.setUsername(user.getUsername());

        // 3.3、将图片路径字符串转换为集合
        String image = ride.getImage();
        List<String> images=new ArrayList<>();
        if (image!=null&&!image.isEmpty())
        {
            images = Arrays.asList(image.split(","));
        }
        rideInfoDTO.setImages(images);

        // 返回结果
        return rideInfoDTO;
    }

    /**
     * 加入骑行团
     *
     * @param id
     */
    @Override
    @Transactional
    public void join(Integer id)
    {
        // 1、获取当前用户信息
        Integer userId = BaseContext.getCurrentId();
        User user = Db.lambdaQuery(User.class)
                .eq(User::getId, userId)
                .one();

        // 2、获取当前用户的信用状态
        // 2.1、如果当前用户处于冻结状态，则无法加入骑行团
        Integer credit = user.getCredit();
        if (Objects.equals(credit, StatusConstant.DISABLED))
        {
            throw new BaseException(MessageConstant.CREDIT_LIMIT);
        }

        // 3、获取骑行团信息
        Ride ride = getById(id);

        // 4、判断当前用户当天有无其他骑行活动（一天只能参与一场活动）
        // 4.1、获取当前用户参与的骑行团信息
        List<RideDetail> list = Db.lambdaQuery(RideDetail.class)
                .eq(RideDetail::getUserId, userId)
                .list();

        // 4.2、判断当天有无其他骑行活动（一天只能参与一场活动）
        if (list != null && !list.isEmpty())
        {
            List<Integer> rideIds = list.stream()
                    .map(RideDetail::getRideId)
                    .toList();
            Set<LocalDate> departureTimes = lambdaQuery()
                    .in(Ride::getId, rideIds)
                    .list()
                    .stream()
                    .map(l ->
                    {
                        return l.getDepartureTime().toLocalDate();
                    })
                    .collect(Collectors.toSet());

            // 4.3、如果当天有其他骑行活动，不允许加入
            if (departureTimes.contains(ride.getDepartureTime().toLocalDate()))
            {
                throw new BaseException(MessageConstant.BUSY_DAY);
            }
        }

        // 5、如果用户帐号状态正常且当天无其他骑行活动，则判断该活动是否还有名额
        // 5.1、如果还有名额，则成功加入骑行团
        if (ride.getParticipants() < ride.getMaxPeople())
        {
            // 骑行明细表添加
            RideDetail rideDetail = new RideDetail();
            rideDetail.setRideId(id);
            rideDetail.setUserId(userId);
            Db.save(rideDetail);

            // 骑行团人数+1
            Integer participants = ride.getParticipants() + 1;
            lambdaUpdate()
                    .eq(Ride::getId, id)
                    .set(Ride::getParticipants, participants)
                    .update();

            // 返回
            return;
        }

        // 5.2、否则提醒人数已满
        throw new BaseException(MessageConstant.TOO_HOT);
    }

    /**
     * 骑行团信息分页查询
     *
     * @param rideInfoPageQuery
     * @return
     */
    @Override
    public PageResult page(RideInfoPageQuery rideInfoPageQuery)
    {
        // 1、获取分页参数
        Integer page = rideInfoPageQuery.getPage();
        Integer pageSize = rideInfoPageQuery.getPageSize();
        String username = rideInfoPageQuery.getUsername();
        String name = rideInfoPageQuery.getName();
        Integer status = rideInfoPageQuery.getStatus();
        Page<Ride> p = Page.of(page, pageSize);

        // 2、根据活动名称与审核状态进行筛选（如有）
        lambdaQuery()
                .eq(status != null, Ride::getStatus, status)
                .like(name != null && !name.isEmpty(), Ride::getName, name)
                .orderByAsc(Ride::getCreateTime)
                .page(p);

        // 2.1、如果结果为空，则返回提示信息
        if (p.getTotal() == 0)
        {
            throw new BaseException(MessageConstant.EMPTY_RESULT);
        }

        // 3、对结果进行属性补充
        // 3.1、获取这些活动的发起者id
        List<Ride> rides = p.getRecords();
        Set<Integer> userIds = rides.stream()
                .map(Ride::getUserId)
                .collect(Collectors.toSet());

        // 3.2、获取这些活动的发起者
        List<User> users = Db.lambdaQuery(User.class)
                .in(User::getId, userIds)
                .list();

        // 3.3、获取发起者id和发起者电话组成的Map
        Map<Integer, String> phones = users.stream()
                .filter(l -> l.getPhone() != null && !l.getPhone().isEmpty())
                .collect(Collectors.toMap(User::getId, User::getPhone));

        // 3.4、获取发起者id和发起者用户名组成的Map
        Map<Integer, String> usernames = users.stream()
                .filter(l -> l.getUsername() != null && !l.getUsername().isEmpty())
                .collect(Collectors.toMap(User::getId, User::getUsername));

        // 3.5、补充属性（用户名和联系方式）
        List<RideInfoDTO> rideInfoDTOS = new ArrayList<>();
        rides.stream()
                .forEach(l ->
                {
                    RideInfoDTO rideInfoDTO = new RideInfoDTO();
                    BeanUtils.copyProperties(l, rideInfoDTO);
                    rideInfoDTO.setUsername(usernames.get(l.getUserId()));
                    rideInfoDTO.setPhone(phones.get(l.getUserId()));
                    rideInfoDTOS.add(rideInfoDTO);
                });

        // 5、对用户名进行模糊匹配筛选
        if (username != null && !username.isEmpty())
        {
            List<RideInfoDTO> list = rideInfoDTOS.stream()
                    .filter(l -> l.getUsername().contains(username))
                    .toList();

            // 5.1、如果结果为空，返回提示信息
            if (list.isEmpty())
            {
                throw new BaseException(MessageConstant.EMPTY_RESULT);
            }

            // 5.2、封装分页查询结果
            return PageResult.builder()
                    .total((long) list.size())
                    .records(list)
                    .build();
        }

        // 6、封装分页查询结果
        return PageResult.builder()
                .total(p.getTotal())
                .records(rideInfoDTOS)
                .build();
    }

    /**
     * 根据id查看骑行团信息详情
     *
     * @param id
     * @return
     */
    @Override
    public RideInfoDTO getone(Integer id)
    {
        // 1、根据id查询骑行团信息
        Ride ride = getById(id);

        // 2、获取发起者信息
        Integer userId = ride.getUserId();
        User user = Db.lambdaQuery(User.class)
                .eq(User::getId, userId)
                .one();

        // 3、封装结果
        // 3.1、属性拷贝
        RideInfoDTO rideInfoDTO=new RideInfoDTO();
        BeanUtils.copyProperties(ride, rideInfoDTO);

        // 3.2、属性补充
        rideInfoDTO.setUsername(user.getUsername());
        if (user.getPhone() != null && !user.getPhone().isEmpty())
        {
            rideInfoDTO.setPhone(user.getPhone());
        }

        // 3.3、将图片路径字符串转换为集合
        String image = ride.getImage();
        List<String> images=new ArrayList<>();
        if (image != null && !image.isEmpty())
        {
            images = Arrays.asList(image.split(","));
        }
        rideInfoDTO.setImages(images);

        // 4、返回结果
        return rideInfoDTO;
    }

    /**
     * 审核骑行团信息
     *
     * @param id
     * @param status
     */
    @Override
    @Transactional
    public void check(Integer id, String reason, Integer status)
    {
        // 1、如果审核状态为驳回，发起者发起骑行次数-1
        if (Objects.equals(status, StatusConstant.REJECTED))
        {
            // 1.1、获取骑行信息
            Ride ride = getById(id);

            // 1.2、获取发起者信息
            Integer userId = ride.getUserId();
            User user = Db.lambdaQuery(User.class)
                    .eq(User::getId, userId)
                    .one();

            // 1.3、将发起者发起骑行次数-1
            Integer rideTimes = user.getRideTimes() - 1;
            Db.lambdaUpdate(User.class)
                    .eq(User::getId,userId)
                    .set(User::getRideTimes,rideTimes)
                    .update();
        }

        // 2、更新审核状态
        lambdaUpdate()
                .eq(Ride::getId, id)
                .set(Ride::getStatus, status)
                .set(reason != null && !reason.isEmpty(), Ride::getReason, reason)
                .update();
    }

    /**
     * 查询我的骑行活动
     *
     * @param status
     * @return
     */
    @Override
    public List<RideInfoDTO> history(Integer status)
    {
        // 1、获取用户信息
        Integer userId = BaseContext.getCurrentId();

        // 2、获取该用户的所有活动
        // 2.1、获取该用户参与的活动id
        List<RideDetail> rideDetails = Db.lambdaQuery(RideDetail.class)
                .eq(RideDetail::getUserId, userId)
                .list();
        List<Integer> rideIds = rideDetails.stream()
                .map(RideDetail::getRideId)
                .toList();

        // 2.2、如果结果为空，返回提示信息
        if (rideIds.isEmpty())
        {
            throw new BaseException(MessageConstant.EMPTY_RESULT);
        }

        // 2.3、根据活动id获取这些活动
        List<Ride> rides = lambdaQuery()
                .in(Ride::getId, rideIds)
                .list();

        // 3、将查询结果封装
        List<RideInfoDTO> rideInfoDTOS = new ArrayList<>();
        rides.stream()
                .forEach(l ->
                {
                    // 3.1、属性拷贝
                    RideInfoDTO rideInfoDTO=new RideInfoDTO();
                    BeanUtils.copyProperties(l, rideInfoDTO);

                    // 3.2、将图片路径字符串转换为集合
                    String image = l.getImage();
                    List<String> images=new ArrayList<>();
                    if (image != null && !image.isEmpty())
                    {
                        images = Arrays.asList(image.split(","));
                    }
                    rideInfoDTO.setImages(images);

                    // 3.3、将DTO存入结果DTOS中
                    rideInfoDTOS.add(rideInfoDTO);
                });

        // 4、判断查询类型
        List<RideInfoDTO> result = new ArrayList<>();
        // 4.1、如果查询类型为全部
        if (Objects.equals(status, StatusConstant.ALL))
        {
            result = rideInfoDTOS.stream()
                    // 按出发时间升序，展示最近
                    .sorted(Comparator.comparing(RideInfoDTO::getDepartureTime))
                    .toList();
        }

        // 4.2、如果查询类型为未开始
        if (Objects.equals(status, StatusConstant.PREPARED))
        {
            result = rideInfoDTOS.stream()
                    .filter(l -> l.getDepartureTime().isAfter(LocalDateTime.now()))
                    // 按出发时间升序，展示最近
                    .sorted(Comparator.comparing(RideInfoDTO::getDepartureTime))
                    .toList();
        }

        // 4.3、如果查询类型为已结束
        if (Objects.equals(status, StatusConstant.FINISHED))
        {
            result = rideInfoDTOS.stream()
                    .filter(l -> l.getDepartureTime().isBefore(LocalDateTime.now()))
                    // 按出发时间升序，展示最近
                    .sorted(Comparator.comparing(RideInfoDTO::getDepartureTime))
                    .toList();
        }

        // 5、返回结果
        // 5.1、如果筛选后结果为空，返回提示消息
        if (result.isEmpty())
        {
            throw new BaseException(MessageConstant.EMPTY_RESULT);
        }

        // 5.2、返回结果
        return result;
    }
}
