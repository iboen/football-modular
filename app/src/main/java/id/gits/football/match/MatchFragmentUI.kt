package id.gits.football.match

import android.graphics.Typeface
import android.support.v4.app.Fragment
import android.view.Gravity
import id.gits.football.R
import id.gits.football.data.Match
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class MatchFragmentUI constructor(var match: Match?) : AnkoComponent<Fragment> {
    companion object {
        const val homeLogoId = 1
        const val awayLogoId = 2
        const val swipeId = 3
    }

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        swipeRefreshLayout {
            id = swipeId

            scrollView {
                verticalLayout {
                    lparams(matchParent, matchParent)
                    padding = dip(16)

                    gravity = Gravity.CENTER_HORIZONTAL

                    textView {
                        text = match?.toFormattedDate()
                        textSize = 16f
                        textColorResource = R.color.colorPrimary
                    }.lparams{
                        gravity = Gravity.CENTER
                    }

                    view {
                        backgroundColorResource = R.color.colorBackgroundGrey
                    }.lparams(matchParent, dip(1)) {
                        topMargin = dip(16)
                        bottomMargin = dip(16)
                    }

                    // scores
                    linearLayout {

                        textView {
                            text = if (match?.isPast == true) match?.intHomeScore.toString() else "-"
                            textSize = 24f
                            gravity = Gravity.END
                        }.lparams {
                            weight = 1f
                        }

                        textView {
                            text = ctx.getString(R.string.schedules_vs)
                            textSize = 20f
                        }.lparams {
                            leftMargin = dip(16)
                            rightMargin = dip(16)
                        }

                        textView {
                            text = if (match?.isPast == true) match?.intAwayScore.toString() else "-"
                            textSize = 24f
                        }.lparams {
                            weight = 1f
                        }
                    }

                    // club detail
                    linearLayout {

                        verticalLayout {
                            imageView {
                                id = homeLogoId
                                imageResource = R.color.colorError
                            }.lparams(dip(72), dip(72)) {
                                gravity = Gravity.CENTER
                            }

                            textView {
                                textSize = 20f
                                text = match?.strHomeTeam
                                textColorResource = R.color.colorPrimary
                            }.lparams {
                                gravity = Gravity.CENTER
                            }

                            textView {
                                text = match?.strHomeFormation
                            }.lparams {
                                gravity = Gravity.CENTER
                            }
                        }.lparams {
                            weight = 1f
                        }


                        space {
                        }.lparams(width = dip(48))

                        verticalLayout {
                            gravity = Gravity.CENTER

                            imageView {
                                id = awayLogoId
                                imageResource = R.color.colorError
                            }.lparams(dip(72), dip(72)) {
                                gravity = Gravity.CENTER
                            }

                            textView {
                                textSize = 20f
                                text = match?.strAwayTeam
                                textColorResource = R.color.colorPrimary
                            }.lparams {
                                gravity = Gravity.CENTER
                            }

                            textView {
                                text = match?.strAwayFormation
                            }.lparams {
                                gravity = Gravity.CENTER
                            }
                        }.lparams {
                            weight = 1f
                        }
                    }

                    // this section only visible if last match
                    if (match?.isPast == true) {
                        view {
                            backgroundColorResource = R.color.colorBackgroundGrey
                        }.lparams(matchParent, dip(1)) {
                            bottomMargin = dip(16)
                        }

                        // goals
                        linearLayout {
                            verticalLayout {
                                match?.strHomeGoalDetails?.split(";")?.forEach {
                                    textView {
                                        text = it
                                    }
                                }
                            }.lparams {
                                weight = 1f
                                width = 0
                            }

                            textView {
                                text = context.getString(R.string.match_goals)
                                typeface = Typeface.DEFAULT_BOLD
                                textColorResource = R.color.colorPrimary
                            }.lparams {
                                leftMargin = dip(4)
                                rightMargin = dip(4)
                            }

                            verticalLayout {
                                match?.strAwayGoalDetails?.split(";")?.forEach {
                                    textView {
                                        text = it
                                        gravity = Gravity.END
                                    }.lparams {
                                        gravity = Gravity.END
                                    }
                                }
                            }.lparams {
                                weight = 1f
                                width = 0
                            }
                        }

                        // shots
                        linearLayout {
                            textView {
                                text = match?.intHomeShots.toString()
                            }.lparams {
                                weight = 1f
                                width = 0
                            }

                            textView {
                                text = context.getString(R.string.match_shots)
                                typeface = Typeface.DEFAULT_BOLD
                                textColorResource = R.color.colorPrimary
                            }.lparams {
                                leftMargin = dip(4)
                                rightMargin = dip(4)
                            }

                            textView {
                                text = match?.intAwayShots.toString()
                                gravity = Gravity.END
                            }.lparams {
                                weight = 1f
                                width = 0
                            }
                        }

                        view {
                            backgroundColorResource = R.color.colorBackgroundGrey
                        }.lparams(matchParent, dip(1)) {
                            topMargin = dip(16)
                            bottomMargin = dip(16)
                        }

                        //LINEUPS

                        textView {
                            text = context.getString(R.string.match_lineups)
                            typeface = Typeface.DEFAULT_BOLD
                            textSize = 20f
                        }.lparams {
                            gravity = Gravity.CENTER
                            bottomMargin = dip(16)
                        }

                        // Goalkeeper
                        linearLayout {
                            textView {
                                text = match?.strHomeLineupGoalkeeper?.replace(";", "")
                            }.lparams {
                                weight = 1f
                                width = 0
                            }

                            textView {
                                text = context.getString(R.string.match_goalkeeper)
                                typeface = Typeface.DEFAULT_BOLD
                                textColorResource = R.color.colorPrimary
                            }.lparams {
                                leftMargin = dip(4)
                                rightMargin = dip(4)
                            }

                            textView {
                                text = match?.strAwayLineupGoalkeeper?.replace(";", "")
                                gravity = Gravity.END
                            }.lparams {
                                weight = 1f
                                width = 0
                            }
                        }.lparams(matchParent, wrapContent) {
                            bottomMargin = dip(16)
                        }

                        // Defense
                        linearLayout {
                            verticalLayout {
                                match?.strHomeLineupDefense?.split("; ")?.forEach {
                                    textView {
                                        text = it
                                    }
                                }
                            }.lparams {
                                weight = 1f
                                width = 0
                            }

                            textView {
                                text = context.getString(R.string.match_defender)
                                typeface = Typeface.DEFAULT_BOLD
                                textColorResource = R.color.colorPrimary
                            }.lparams {
                                leftMargin = dip(4)
                                rightMargin = dip(4)
                            }

                            verticalLayout {
                                match?.strAwayLineupDefense?.split("; ")?.forEach {
                                    textView {
                                        text = it
                                        gravity = Gravity.END
                                    }.lparams {
                                        gravity = Gravity.END
                                    }
                                }
                            }.lparams {
                                weight = 1f
                                width = 0
                            }
                        }.lparams(matchParent, wrapContent) {
                            bottomMargin = dip(16)
                        }

                        // Midfield
                        linearLayout {
                            verticalLayout {
                                match?.strHomeLineupMidfield?.split("; ")?.forEach {
                                    textView {
                                        text = it
                                    }
                                }
                            }.lparams {
                                weight = 1f
                                width = 0
                            }

                            textView {
                                text = context.getString(R.string.match_midfielder)
                                typeface = Typeface.DEFAULT_BOLD
                                textColorResource = R.color.colorPrimary
                            }.lparams {
                                leftMargin = dip(4)
                                rightMargin = dip(4)
                            }

                            verticalLayout {
                                match?.strAwayLineupMidfield?.split("; ")?.forEach {
                                    textView {
                                        text = it
                                        gravity = Gravity.END
                                    }.lparams {
                                        gravity = Gravity.END
                                    }
                                }
                            }.lparams {
                                weight = 1f
                                width = 0
                            }
                        }.lparams(matchParent, wrapContent) {
                            bottomMargin = dip(16)
                        }

                        // Forward
                        linearLayout {
                            verticalLayout {
                                match?.strHomeLineupForward?.split("; ")?.forEach {
                                    textView {
                                        text = it
                                    }
                                }
                            }.lparams {
                                weight = 1f
                                width = 0
                            }

                            textView {
                                text = context.getString(R.string.match_forwarder)
                                typeface = Typeface.DEFAULT_BOLD
                                textColorResource = R.color.colorPrimary
                            }.lparams {
                                leftMargin = dip(4)
                                rightMargin = dip(4)
                            }

                            verticalLayout {
                                match?.strAwayLineupForward?.split("; ")?.forEach {
                                    textView {
                                        text = it
                                        gravity = Gravity.END
                                    }.lparams {
                                        gravity = Gravity.END
                                    }
                                }
                            }.lparams {
                                weight = 1f
                                width = 0
                            }
                        }.lparams(matchParent, wrapContent) {
                            bottomMargin = dip(16)
                        }

                        // Substitutes
                        linearLayout {
                            verticalLayout {
                                match?.strHomeLineupSubstitutes?.split("; ")?.forEach {
                                    textView {
                                        text = it
                                    }
                                }
                            }.lparams {
                                weight = 1f
                                width = 0
                            }

                            textView {
                                text = context.getString(R.string.match_substitutes)
                                typeface = Typeface.DEFAULT_BOLD
                                textColorResource = R.color.colorPrimary
                            }.lparams {
                                leftMargin = dip(4)
                                rightMargin = dip(4)
                            }

                            verticalLayout {
                                match?.strAwayLineupSubstitutes?.split("; ")?.forEach {
                                    textView {
                                        text = it
                                        gravity = Gravity.END
                                    }.lparams {
                                        gravity = Gravity.END
                                    }
                                }
                            }.lparams {
                                weight = 1f
                                width = 0
                            }
                        }.lparams(matchParent, wrapContent) {
                            bottomMargin = dip(16)
                        }
                    }
                }
            }
        }
    }

}
