const User = require('../models/user');
const Animal = require('../models/animal');
const Walking = require('../models/walking');
const Record = require('../models/record');
const UserChallenge = require('../models/userChallenge');


exports.createWalking = async (req, res, next) => {
    const userData = req.userToken.id;
    const calorie = req.body.calorie;
    const distance = req.body.distance;
    const walkingTime = req.body.walkingTime;
    const addressAdmin = req.body.addressAdmin;
    const addressLocality = req.body.addressLocality;
    const addressThoroughfare = req.body.addressThoroughfare;
    const animal = req.body.animal;
    let route = [];
    let toiletLoc = []
    let walkingAmounts = [];

    const requestRoute = req.body.route;
    const requestToiletLoc = req.body.toiletLoc;
    const requestWalkingAmounts = req.body.walkingAmounts;

    // 산책 경로 위도, 경도 후처리
    requestRoute.forEach((loc) => {
        route.push({lat: loc[0], lon: loc[1]});
    })
    
    // 배변 활동 좌표 위도, 경도 후처리
    requestToiletLoc.forEach((loc) => {
        toiletLoc.push({lat: loc[0], lon: loc[1]});
    })

    // 산책량(%) 후처리
    for (wa in requestWalkingAmounts) {
        walkingAmounts.push({ animal: animal[wa], amount: requestWalkingAmounts[wa]});
    }

    try {
        const user = await User.findOne({ _id: userData });
        // 새로운 산책 등록
        const walkingRegister = await new Walking({
            calorie,
            distance,
            walkingTime,
            walkingAmounts,
            addressAdmin,
            addressLocality,
            addressThoroughfare,
            animal,
            user,
            route,
            toiletLoc,
        });
        const resultReg = await Walking.create(walkingRegister);
        console.log(resultReg);

        let recordWalkingAmount = 0;

        walkingAmounts.forEach((walking) => {
            recordWalkingAmount += walking.amount
        })
        recordWalkingAmount / walkingAmounts.length

        // 유저가 챌린지에 참여 중이면 기록 갱신
        const challengeList = await UserChallenge.find({ userId: userData });

        if (challengeList) {
            challengeList.forEach(async challenge => {
                const challengeId = challenge.challengeId;
                
                const record = await Record.findOne({ challenge: challengeId });
                const walkingCount = record.walkingCount;
                const newWalkingCount = walkingCount + 1;
                const walkingAvg = record.walkingAvg;
                const score = record.score;
                score += 75 + walkingAvg;
                const newWalkingAvg = (walkingCount * walkingAvg + recordWalkingAmount) / newWalkingCount;

                await record.updateOne({ $set: { walkingCount: newWalkingCount, walkingAvg: newWalkingAvg, score: score }});
            })
        };

        res.json({ walkingId: resultReg._id});
    } catch (err) {
        console.log(err);
        res.json({ error: 1 });
    }
}

// 산책 디테일 정보
exports.showRoute = async (req, res, next) => {
    const walkingId = req.query.walkingId;

    try {
        const walking = await Walking.findOne({ _id: walkingId }).select("route toiletLoc");

        res.json({ route: walking.route, toiletLoc: walking.toiletLoc });
    } catch (err) {
        console.log(err);
        res.json({ error: 1 });
    }
}