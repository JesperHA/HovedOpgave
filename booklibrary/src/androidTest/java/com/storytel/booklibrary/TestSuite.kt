package com.storytel.booklibrary

import org.junit.runner.RunWith
import org.junit.runners.Suite


// Runs all unit tests.
@RunWith(Suite::class)
@Suite.SuiteClasses(SLBookDatabaseTest::class)
class UnitTestSuite