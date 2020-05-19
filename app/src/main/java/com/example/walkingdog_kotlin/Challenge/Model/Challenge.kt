package com.example.walkingdog_kotlin.Challenge.Model

import com.example.walkingdog_kotlin.ReformatDate

class Challenge () {
    var title: String = ""
    var comment:String = ""
    var period:String = ""
    var number:String = ""

    constructor(challenge: ChallengeModel): this() {
        this.title = challenge.title
        this.comment = challenge.content
        this.period = ReformatDate("MM월 dd일", challenge.start_date) + " ~ " +
                ReformatDate("MM월 dd일", challenge.end_date)
        this.number = challenge.population.toString()
    }
}