const User = require('../models/user');
const Animal = require('../models/animal');
const Walking = require('../models/walking');
const Record = require('../models/record');
const UserChallenge = require('../models/userChallenge');


exports.createWalking = async (req, res, next) => {
    console.log(req.body)
    // const userData = req.userToken.id;
    const calorie = 0;
    const distance = 0;
    const walkingTime = 0;
    const walkingAmounts = [];
    const addressAdmin = "경기도";
    const addressLocality = "수원시"
    const addressThoroughfare = "연무동"
    const animal = ["까까", "브라우니"];
    const userData = "5eba8b5ca76e3e20f4b0659e";
    // const requestFile = req.file;
    let routeImage = "";
    let route = [];
    let toiletLoc = []
    const requestWalkingAmounts = [40, 50];
    const requestRoute = [[123.123, 123.555], [11.22231, 451.222]];
    const requestToiletLoc = [[123.123, 123.555], [11.22231, 451.222]];


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
        // console.log(challengeList)
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

        res.json({});
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
        console.log(walking)
        res.json({ route: walking.route, toiletLoc: walking.toiletLoc });
    } catch (err) {
        console.log(err);
        res.json({ error: 1 });
    }
}