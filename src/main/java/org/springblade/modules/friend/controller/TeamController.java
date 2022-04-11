/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.springblade.modules.friend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;

import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.friend.entity.Friend;
import org.springblade.modules.friend.service.IFriendService;
import org.springblade.modules.friend.vo.DictVo;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.service.IUserService;
import org.springblade.modules.system.vo.DictVO;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.friend.entity.Team;
import org.springblade.modules.friend.vo.TeamVO;
import org.springblade.modules.friend.service.ITeamService;
import org.springblade.core.boot.ctrl.BladeController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 控制器
 *
 * @author BladeX
 * @since 2022-04-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("blade-friend/team")
@Api(value = "", tags = "接口")
public class TeamController extends BladeController {

	private final ITeamService teamService;

	private final IFriendService friendService;

	private final IUserService userService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入team")
	public R<Team> detail(Team team) {
		Team detail = teamService.getOne(Condition.getQueryWrapper(team));
		return R.data(detail);
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入team")
	public R<IPage<Team>> list(Team team, Query query) {
		IPage<Team> pages = teamService.page(Condition.getPage(query), Condition.getQueryWrapper(team));
		return R.data(pages);
	}

	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入team")
	public R<IPage<TeamVO>> page(TeamVO team, Query query) {
		IPage<TeamVO> pages = teamService.selectTeamPage(Condition.getPage(query), team);
		return R.data(pages);
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入team")
	public R save(@Valid @RequestBody Team team) {
		return R.status(teamService.save(team));
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入team")
	public R update(@Valid @RequestBody Team team) {
		return R.status(teamService.updateById(team));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入team")
	public R submit(@Valid @RequestBody Team team) {
		return R.status(teamService.saveOrUpdate(team));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(teamService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 分页
	 */
	@GetMapping("/friendDict")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入team")
	public R<List<DictVo>> friendDict() {
		Long userId = AuthUtil.getUserId();
		QueryWrapper<Friend> wrapper = new QueryWrapper<>();
		wrapper.eq("create_user", userId).eq("status", 0);
		List<DictVo> list = new ArrayList<>();
		List<Friend> friends = friendService.getBaseMapper().selectList(wrapper);
		List<User> list1 = userService.list();
		friends.stream().forEach(a -> {
			DictVo dictVo = new DictVo();
			dictVo.setLabel(a.getName());
			dictVo.setValue(a.getId().intValue());
			list.add(dictVo);
		});
		list1.stream().forEach(a -> {
			DictVo dictVo = new DictVo();
			dictVo.setLabel(a.getName());
			dictVo.setValue(a.getId().intValue());
			list.add(dictVo);
		});
		ArrayList<DictVo> collect = list.stream().collect(Collectors.collectingAndThen
			(Collectors.toCollection(() ->
					new TreeSet<>(Comparator.comparing(t -> t.getLabel()))),
				ArrayList::new));
		return R.data(collect);
	}


}
