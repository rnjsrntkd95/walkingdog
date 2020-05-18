const User = require('../models/user');
const Record = require('../models/record');
const Challenge = require('../models/challenge');
const Animal = require('../models/animal');
const UserChallenge = require('../models/userChallenge');

exports.createChallenge = async (req, res, next) => {
    // const userData = req.userToken.id;
    // const title = req.body.title;
    // const populationLimit = req.body.populationLimit;
    // const endDate = req.body.endDate;
    
    const title = "새로운 챌린지";
    const populationLimit = 50;
    const start_date = '2020-05-19';
    const end_date = "2020-08-01";
    const userData = '5eba8b5ca76e3e20f4b0659e';

    try {
        const producer = await User.findOne({ _id: userData });
        const newChallenge = await new Challenge({
            title,
            start_date,
            end_date,
            populationLimit,
            producer,
        })

        const challengeId = await Challenge.create(newChallenge);

        const connection = await new UserChallenge({
            userId: producer._id,
            challengeId: challengeId._id,
        });

        const resultReg = await UserChallenge.create(connection);
        
        res.json({});
    } catch (err) {
        console.log(err);

        res.json({ error: 1 });
    }
}


exports.joinChallenge = async (req, res, next) => {
    // const userData = req.userToken.id;
    const userData = '5eba8b5ca76e3e20f4b0659e';
    const challengeId = '5ec26c24964981578443d490';
    try {
        const user = await User.findOne({ _id: userData });
        const challenge = await Challenge.findOneAndUpdate({ _id: challengeId }, {$inc: { population: 1 }}, { new: true });
    
        const join = await new UserChallenge({
            userId: user._id,
            challengeId: challenge._id,
        })
        const resultJoin = await UserChallenge.create(join);

        const newRecord = await Record({
            user,
            challenge,
            challengeTitle: challenge.title,
        })
        const resultNewRecord = await Record.create(newRecord);

        res.json({});
    } catch (err) {
        console.log(err);
        res.json({ error: 1 });
    }
}

exports.searchChallenge = async (req, res, next) => {

    try {
        // 정렬: 시작 날짜가 현재 날짜와 가까운 순서로 반환
        const challenges = await Challenge.find({}).sort('-start_date');
        
        res.json({ challenges });
    } catch (err) {
        console.log(err);
        res.json({ error : 1 });
        
    }
}