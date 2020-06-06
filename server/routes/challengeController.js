const User = require("../models/user");
const Record = require("../models/record");
const Challenge = require("../models/challenge");
const Animal = require("../models/animal");
const UserChallenge = require("../models/userChallenge");

exports.createChallenge = async (req, res, next) => {
  console.log(req.body);
  // const userData = req.userToken.id;
  const userData = req.body.userToken;
  const title = req.body.title;
  const content = req.body.content;
  const breed = req.body.breed;
  const populationLimit = req.body.populationLimit;
  const start_date = req.body.start_date;
  const end_date = req.body.end_date;

  // const title = "새로운 챌린지";
  // const content = "챌린지 모여라~";
  // const populationLimit = 50;
  // const start_date = '2020-05-19';
  // const end_date = "2020-08-01";
  // const userData = '5eba8b5ca76e3e20f4b0659e';
  // challenge의 고유 id를 반환해줘야 함.

  try {
    if (userData == "0") {
      res.json({
        message: "user not found",
        error: 1,
      });
    }
    const producer = await User.findOne({ _id: userData });

    const newChallenge = await new Challenge({
      title,
      content,
      breed,
      start_date,
      end_date,
      populationLimit,
      producer,
    });

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
};

exports.joinChallenge = async (req, res, next) => {
  console.log(req.body);
  const userData = req.body.userToken;
  const challengeId = req.body._id;

  // const userData = "5eba8b5ca76e3e20f4b0659e";
  // const challengeId = "5ec28c44abe2802874204a43";
  try {
    const user = await User.findOne({ _id: userData });
    // 같은 챌린지 중복 참여 방지
    const checkingOverlap = await UserChallenge.find({
      userId: user._id,
      challengeId: challengeId,
    });
    console.log(checkingOverlap.length);
    if (checkingOverlap.length === 0) {
      const challenge = await Challenge.findOneAndUpdate(
        { _id: challengeId },
        { $inc: { population: 1 } },
        { new: true }
      );

      const join = await new UserChallenge({
        userId: user._id,
        challengeId: challenge._id,
      });
      const resultJoin = await UserChallenge.create(join);

      const newRecord = await Record({
        user,
        challenge,
        challengeTitle: challenge.title,
      });
      const resultNewRecord = await Record.create(newRecord);

      res.json({});
    } else {
      res.json({ error: 2, msg: "이미 가입된 챌린지가 존재합니다." });
    }
  } catch (err) {
    console.log(err);
    res.json({ error: 1 });
  }
};

exports.searchChallenge = async (req, res, next) => {
  // res.json({
  //   challenges: [
  //     {
  //       title: "새로운 챌린지",
  //       content: "챌린지 모여라~",
  //       population: 50,
  //       start_date: "2020-06-02",
  //       end_date: "2020-06-16",
  //       _id: "5eba8b5ca76e3e20f4b0659e",
  //     },
  //   ],
  //   popularChallenge: [
  //     {
  //       title: "인기 챌린지",
  //       content: "챌린지 모여라~",
  //       population: 50,
  //       start_date: "2020-06-02",
  //       end_date: "2020-06-16",
  //       _id: "5eba8b5ca76e3e20f4b0659e",
  //     },
  //   ],
  //   error: 1,
  // });
  try {
    // 정렬: 시작 날짜가 현재 날짜와 가까운 순서로 반환
    const challenges = await Challenge.find({}).sort("-start_date");
    // 인기 챌린지
    const popularChallenge = await Challenge.findOne({}).sort("-population");
    console.log(challenges);
    console.log(popularChallenge);
    res.json({
      challenges,
      popularChallenge,
    });
  } catch (err) {
    console.log(err);
    res.json({ error: 1 });
  }
};

exports.dropChallenge = async (req, res, next) => {
  const userData = req.query.userToken;
  const challengeId = req.query._id;
  // const userData = "5eba8b5ca76e3e20f4b0659e";
  // const challengeId = "5ec28c44abe2802874204a43";
  try {
    const challenge = await Challenge.findByIdAndUpdate(
      { _id: challengeId },
      { $inc: { population: -1 } }
    );
    await UserChallenge.findOneAndDelete({ userId: userData, challengeId });
    await Record.findOneAndDelete({ user: userData, challenge: challengeId });
    res.json({ msg: "성공적으로 탈퇴하였습니다" });
  } catch (err) {
    console.log(err);
    res.json({ error: 1 });
  }
};
