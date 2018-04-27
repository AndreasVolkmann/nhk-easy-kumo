package me.avo

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import me.avo.kumo.productionModule

val testKodein = Kodein {
    import(productionModule, allowOverride = true)

    bind<String>("url", overrides = true) with singleton { "jdbc:sqlite:D:\\Dev\\data\\sqlite\\nhk-easy-test" }


}