package util

import com.github.funczz.ruby_calc.core.model.RubyCalcStateModel
import com.github.funczz.ruby_calc.core.service.RubyService
import com.github.funczz.ruby_calc.data.model.RubyCalStateModelFactory

object RubyCalcStateModelUtil {
    fun new(
        dbTestUtil: DBTestUtil,
        rubyService: RubyService = RubyService(),
    ): RubyCalcStateModel = RubyCalStateModelFactory.new(
        dbResources = dbTestUtil.dbResources,
        rubyService = rubyService,
    )
}
