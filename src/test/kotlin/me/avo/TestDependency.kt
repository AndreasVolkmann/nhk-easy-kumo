package me.avo

import me.avo.kumo.productionModule
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val testKodein = Kodein {
    import(productionModule, allowOverride = true)

    bind<String>("url", overrides = true) with singleton { "jdbc:sqlite:D:\\Dev\\data\\sqlite\\nhk-easy-test" }


}