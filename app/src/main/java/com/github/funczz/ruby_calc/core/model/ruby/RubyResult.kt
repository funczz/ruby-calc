package com.github.funczz.ruby_calc.core.model.ruby

import java.util.Optional

data class RubyResult(

    val success: String = "",

    val failure: Optional<Throwable> = Optional.empty(),

    )
