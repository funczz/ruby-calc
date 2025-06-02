package com.github.funczz.ruby_calc.data.model

import com.github.funczz.ruby_calc.core.model.RubyCalcStateModel
import com.github.funczz.ruby_calc.core.model.answer.AnswerStateModel
import com.github.funczz.ruby_calc.core.model.element.ElementStateModel
import com.github.funczz.ruby_calc.core.model.error.ErrorStateModel
import com.github.funczz.ruby_calc.core.model.problem.ProblemStateModel
import com.github.funczz.ruby_calc.core.model.program.ProgramStateModel
import com.github.funczz.ruby_calc.core.model.ruby.RubyStateModel
import com.github.funczz.ruby_calc.core.service.RubyService
import com.github.funczz.ruby_calc.data.db.RubyCalcDBResources
import com.github.funczz.ruby_calc.data.provider.answer.DBCRUDAnswerDataProvider
import com.github.funczz.ruby_calc.data.provider.answer.DBFindAnswerDataProvider
import com.github.funczz.ruby_calc.data.provider.element.DBRWElementDataProvider
import com.github.funczz.ruby_calc.data.provider.problem.DBCRUDProblemDataProvider
import com.github.funczz.ruby_calc.data.provider.problem.DBFindProblemDataProvider
import com.github.funczz.ruby_calc.data.provider.program.DBCRUDProgramDataProvider
import com.github.funczz.ruby_calc.data.provider.program.DBFindProgramDataProvider
import com.github.funczz.ruby_calc.data.provider.setting.DBRWSettingDataProvider

object RubyCalStateModelFactory {

    fun new(dbResources: RubyCalcDBResources, rubyService: RubyService): RubyCalcStateModel {
        val database = dbResources.database
        val crudProgramDataProvider = DBCRUDProgramDataProvider(database = database)
        val findProgramDataProvider = DBFindProgramDataProvider(database = database)
        val crudProblemDataProvider = DBCRUDProblemDataProvider(database = database)
        val findProblemDataProvider = DBFindProblemDataProvider(database = database)
        val rwElementDataProvider = DBRWElementDataProvider(database = database)
        val crudAnswerDataProvider = DBCRUDAnswerDataProvider(database = database)
        val findAnswerDataProvider = DBFindAnswerDataProvider(database = database)
        val rwSettingDataProvider = DBRWSettingDataProvider(database = database)
        return RubyCalcStateModel(
            programStateModel = ProgramStateModel.new(
                crudProgramDataProvider = crudProgramDataProvider,
                findProgramDataProvider = findProgramDataProvider,
                findProblemDataProvider = findProblemDataProvider,
                rwSettingDataProvider = rwSettingDataProvider,
            ),
            problemStateModel = ProblemStateModel.new(
                crudProblemDataProvider = crudProblemDataProvider,
                crudProgramDataProvider = crudProgramDataProvider,
                findProblemDataProvider = findProblemDataProvider,
                rwSettingDataProvider = rwSettingDataProvider,
                rwElementDataProvider = rwElementDataProvider,
                findAnswerDataProvider = findAnswerDataProvider,
            ),
            elementStateModel = ElementStateModel.new(
                rwElementDataProvider = rwElementDataProvider
            ),
            answerStateModel = AnswerStateModel.new(
                crudAnswerDataProvider = crudAnswerDataProvider,
                findAnswerDataProvider = findAnswerDataProvider,
            ),
            rubyStateModel = RubyStateModel.new(
                rubyService = rubyService
            ),
            errorStateModel = ErrorStateModel.new(),
        )
    }
}
