package com.tangyujun.delines.test;

import com.tangyujun.delines.annotation.DelinesField;
import com.tangyujun.delines.validation.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

public class Test {
	@DelinesField(regExp = "[a-z]")
	@AssertFalse
	private Boolean ds;

	@DatePast
	private LocalDateTime time;

	@DatePast("20220302")
	private Date time2;

	@StringLength(min = -2, max = -3)
	@StringNotEmpty
	@StringPattern("[]")
	private String sgsd;
}
