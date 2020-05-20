const User = require('../models/user');
const Animal = require('../models/animal');
const Walking = require('../models/walking');
const Record = require('../models/record');
const UserChallenge = require('../models/userChallenge');


exports.createWalking = async (req, res, next) => {
    // const userData = req.userToken.id;
    const calorie = 0;
    const distance = 0;
    const routeImage = "";
    const walkingTime = 0;
    const walkingAmount = 20;
    const location = "인천광역시";
    const animal = ["까까", "브라우니"];
    
    const userData = "5eba8b5ca76e3e20f4b0659e";
    console.log("createWalking 진입");
    try {
        const user = await User.findOne({ _id: userData });
        console.log(user)
        // 새로운 산책 등록
        const walkingRegister = await new Walking({
            calorie,
            distance,
            routeImage,
            walkingTime,
            walkingAmount,
            location,
            animal,
            user,
        });
        const resultReg = await Walking.create(walkingRegister);

        // 유저가 챌린지에 참여 중이면 기록 갱신
        const challengeList = await UserChallenge.find({ userId: userData });
        // console.log(challengeList)
        if (challengeList) {
            challengeList.forEach(async challenge => {
                const challengeId = challenge.challengeId;
                
                const record = await Record.findOne({ challenge: challengeId });
                const walkingCount = record.walkingCount;
                const newWalkingCount = walkingCount + 1;
                const walkingAvg = record.walkingAvg;
                const newWalkingAvg = (walkingCount * walkingAvg + walkingAmount) / newWalkingCount;

                await record.updateOne({ $set: { walkingCount: newWalkingCount, walkingAvg: newWalkingAvg }});
            })
        };

        res.json({});
    } catch (err) {
        console.log(err);
        res.json({ error: 1 });
    }
}