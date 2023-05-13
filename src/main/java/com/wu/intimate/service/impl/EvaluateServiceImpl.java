package com.wu.intimate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.intimate.mapper.EvaluateMapper;
import com.wu.intimate.model.Evaluate;
import com.wu.intimate.service.EvaluateService;
import org.springframework.stereotype.Service;

@Service
public class EvaluateServiceImpl extends ServiceImpl<EvaluateMapper, Evaluate> implements EvaluateService {
}
