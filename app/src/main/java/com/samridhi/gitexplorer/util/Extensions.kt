package com.samridhi.gitexplorer.util


fun String.addRouteArgument(argName: String) =
    this.plus("?").plus(argName).plus("={").plus(argName).plus("}")
fun String.withArg(argName: String, argValue: String) =
    "$this?$argName=${argValue}"