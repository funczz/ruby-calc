package com.github.funczz.ruby_calc.core.model.answer

import java.util.Optional

data class AnswerDetails(
    val answerModel: Optional<AnswerModel> = Optional.empty(),
)
