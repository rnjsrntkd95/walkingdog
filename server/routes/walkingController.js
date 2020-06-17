const User = require('../models/user');
const Animal = require('../models/animal');
const Walking = require('../models/walking');
const Record = require('../models/record');
const UserChallenge = require('../models/userChallenge');
const { request } = require('../app');


exports.createWalking = async (req, res, next) => {
    const userData = req.userToken.id;
    const calories = req.body.calorie;
    const distance = req.body.distance;
    const walkingTime = req.body.walkingTime;
    const addressAdmin = req.body.addressAdmin;
    const addressLocality = req.body.addressLocality;
    const addressThoroughfare = req.body.addressThoroughfare;
    const requestAnimal = req.body.animal;
    let animal = [];
    let amountTemp = [];
    let route = [];
    let toiletLoc = []
    let walkingAmounts = [];
    

    let requestRoute = req.body.route;
    let requestToiletLoc = req.body.toiletLoc;
    const requestWalkingAmounts = req.body.walkingAmounts;

    // 동물 후처리
    if (typeof(requestAnimal) == "string") {
        animal[0] = requestAnimal;
    } else {
        animal = requestAnimal;
    }

    // 산책 경로 위도, 경도 후처리
    requestRoute.forEach((loc) => {
        loc = loc.replace('[', "");
        loc = loc.replace(']', "");
        const location = loc.split(',');
        route.push({ lat: location[0], lon: location[1]});
    })
    if(requestRoute) {
        if (typeof(requestRoute) == "string") {
            requestRoute = requestRoute.replace('[', "");
            requestRoute = requestRoute.replace(']', "");
            const location = requestRoute.split(',');
            route.push({lat: location[0], lon: location[1]});
        } else {
            requestRoute.forEach((loc) => {
                loc = loc.replace('[', "");
                loc = loc.replace(']', "");
                const location = loc.split(',');
                route.push({ lat: location[0], lon: location[1]});
            })
        }
    }
    

    // 배변 활동 좌표 위도, 경도 후처리
    if(requestToiletLoc) {
        if (typeof(requestToiletLoc) == "string") {
            requestToiletLoc = requestToiletLoc.replace('[', "");
            requestToiletLoc = requestToiletLoc.replace(']', "");
            const location = requestToiletLoc.split(',');
            toiletLoc.push({lat: location[0], lon: location[1]});
        } else {
            requestToiletLoc.forEach((loc) => {
                loc = loc.replace('[', "");
                loc = loc.replace(']', "");
                const location = loc.split(',');
                toiletLoc.push({lat: location[0], lon: location[1]});
            })
        }
    }


    // 산책량(%) 후처리
    if (requestWalkingAmounts) {
        if (typeof(requestWalkingAmounts) == "string") {
            amountTemp[0] = requestWalkingAmounts;
        } else {
            amountTemp = requestWalkingAmounts;
        }
        for (wa in amountTemp) {
            walkingAmounts.push({ animal: animal[wa], amount: amountTemp[wa]});
        }
    }



    try {
        const user = await User.findOne({ _id: userData });
        // 새로운 산책 등록
        const walkingRegister = await new Walking({
            calories,
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
            recordWalkingAmount += parseInt(walking.amount);
        })
        recordWalkingAmount = recordWalkingAmount / walkingAmounts.length

        // 유저가 챌린지에 참여 중이면 기록 갱신
        const challengeList = await UserChallenge.find({ userId: userData });
        if (challengeList) {
            challengeList.forEach(async challenge => {
                const challengeId = challenge.challengeId;
                try {
                    const record = await Record.findOne({ user: userData, challenge: challengeId });                
                    const walkingCount = record.walkingCount;
                    const newWalkingCount = walkingCount + 1;
                    const walkingAvg = record.walkingAvg;
                    let score = record.score;
                    score += 75 + parseInt(walkingAvg);
                    const newWalkingAvg = (walkingCount * walkingAvg + recordWalkingAmount) / newWalkingCount;

                    const updateRecordReg = await Record.findOneAndUpdate({ _id: record._id}, { $set: { walkingCount: newWalkingCount, walkingAvg: newWalkingAvg, score: score }}, { timestamps: false });

                } catch (err) {
                    console.log(err)
                }

            })
        } else {
            console.log("가입된 챌린지 없음")
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

exports.showMyStatic = async (req, res, next) => {
    const userData = req.userToken.id;

    try {
        const walkings = await Walking.find({ user: userData }).sort("-date");
        res.json({ walkings });
    } catch (err) {
        console.log(err);
        res.json({ error: 1 });
    }
}