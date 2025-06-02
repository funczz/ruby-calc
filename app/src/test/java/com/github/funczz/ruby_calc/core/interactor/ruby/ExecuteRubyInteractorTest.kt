package com.github.funczz.ruby_calc.core.interactor.ruby

import com.github.funczz.ruby_calc.core.service.RubyService
import com.github.funczz.ruby_calc.core.usecase.ruby.ExecuteRubyUseCase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ExecuteRubyInteractorTest {

    @Test
    fun executeRuby() {
        val expected = "hello world."
        val inputData = ExecuteRubyUseCase.InputData(
            code = "ARGV[0]",
            argv = listOf(expected)
        )
        val actual = useCase(inputData = inputData)

        assertEquals(expected, actual)
    }

    private lateinit var useCase: ExecuteRubyUseCase

    @Before
    fun beforeEach() {
        useCase = ExecuteRubyInteractor(
            rubyService = RubyService()
        )
    }
}
