package com.wu.intimate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.intimate.mapper.EvaluateMapper;
import com.wu.intimate.mapper.NoticeMapper;
import com.wu.intimate.model.Evaluate;
import com.wu.intimate.model.Notice;
import com.wu.intimate.service.EvaluateService;
import com.wu.intimate.service.NoticeService;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
}
