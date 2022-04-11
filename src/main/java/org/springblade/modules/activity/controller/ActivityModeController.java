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
package org.springblade.modules.activity.controller;

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
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.activity.entity.Activity;
import org.springblade.modules.activity.service.IActivityService;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.activity.entity.ActivityMode;
import org.springblade.modules.activity.vo.ActivityModeVO;
import org.springblade.modules.activity.service.IActivityModeService;
import org.springblade.core.boot.ctrl.BladeController;

import java.util.Date;

/**
 * 控制器
 *
 * @author BladeX
 * @since 2022-04-08
 */
@RestController
@AllArgsConstructor
@RequestMapping("blade-activity-mode/activitymode")
@Api(value = "", tags = "接口")
public class ActivityModeController extends BladeController {

	private final IActivityModeService activityModeService;

	private final IActivityService activityService;


	/**
	 * 使用模板
	 */
	@PostMapping("/usermode")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入activityMode")
	public R<ActivityMode> usermode(ActivityMode activityMode) {
		ActivityMode activityModeServiceById = activityModeService.getById(activityMode.getId());
		Activity activity1 = new Activity();
		activity1.setCreateUser(AuthUtil.getUserId());
		activity1.setActivityName(activityModeServiceById.getActivityName());
		activity1.setStatus(1);
		int count = activityService.count(Condition.getQueryWrapper(activity1));
		if (count > 0) {
			return R.fail("请勿重复使用模板！");
		}


		Activity activity = new Activity();
		BeanUtil.copyProperties(activityModeServiceById, activity);
		activity.setId(null);
		activity.setPaymentMethod(1);
		activity.setActivityAddress(activityModeServiceById.getAddress());
		activity.setCreateUserName(AuthUtil.getUserName());
		activity.setCreateUser(AuthUtil.getUserId());
		activity.setUpdateUser(AuthUtil.getUserId());
		activity.setUpdateTime(new Date());
		activity.setCreateTime(new Date());
		return R.status(activityService.save(activity));
	}

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入activityMode")
	public R<ActivityMode> detail(ActivityMode activityMode) {
		ActivityMode detail = activityModeService.getOne(Condition.getQueryWrapper(activityMode));
		return R.data(detail);
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入activityMode")
	public R<IPage<ActivityMode>> list(ActivityMode activityMode, Query query) {
		IPage<ActivityMode> pages = activityModeService.page(Condition.getPage(query), Condition.getQueryWrapper(activityMode));
		return R.data(pages);
	}

	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入activityMode")
	public R<IPage<ActivityModeVO>> page(ActivityModeVO activityMode, Query query) {
		IPage<ActivityModeVO> pages = activityModeService.selectActivityModePage(Condition.getPage(query), activityMode);
		return R.data(pages);
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入activityMode")
	public R save(@Valid @RequestBody ActivityMode activityMode) {
		return R.status(activityModeService.save(activityMode));
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入activityMode")
	public R update(@Valid @RequestBody ActivityMode activityMode) {
		return R.status(activityModeService.updateById(activityMode));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入activityMode")
	public R submit(@Valid @RequestBody ActivityMode activityMode) {
		activityMode.setCreateUserName(AuthUtil.getUserName());
		return R.status(activityModeService.saveOrUpdate(activityMode));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(activityModeService.removeByIds(Func.toLongList(ids)));
	}


}
