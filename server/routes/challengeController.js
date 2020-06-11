const User = require("../models/user");
const Record = require("../models/record");
const Challenge = require("../models/challenge");
const Animal = require("../models/animal");
const UserChallenge = require("../models/userChallenge");

exports.createChallenge = async (req, res, next) => {
  const userData = req.userToken.id;
  const title = req.body.title;
  const content = req.body.content;
  const breed = req.body.breed;
  const populationLimit = req.body.populationLimit;
  const start_date = req.body.start_date;
  const end_date = req.body.end_date;

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
  const userData = req.userTokne.id;
  const challengeId = req.body._id;

  try {
    const user = await User.findOne({ _id: userData });
    // 같은 챌린지 중복 참여 방지
    const checkingOverlap = await UserChallenge.find({
      userId: user._id,
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

      res.json({ error: 0, msg: "챌린지에 가입하셨습니다" });
    } else {
      res.json({ error: 2, msg: "이미 가입된 챌린지가 존재합니다." });
    }
  } catch (err) {
    console.log(err);
    res.json({ error: 1 });
  }
};

exports.searchChallenge = async (req, res, next) => {
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
  const userData = req.userToken.id;
  const challengeId = req.query._id;

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

// 챌린지에 참여하는 유저 정보를 가져와야함.
exports.usersInChallenge = async (req, res, next) => {
  const userData = req.userToken.id;
  const challengeId = req.body.challengeId;
  try {
    const myChallenge = await UserChallenge.find({
      userId: userData,
    }).populate("userId", "nickname");
    const challengeUserList = await UserChallenge.find({
      challengeId: challengeId,
    })
      .populate("userId", "nickname")
      .sort("-score");
    res.json({ myChallenge, challengeUserList });
    console.log(myChallenge, challengeUserList);
  } catch (err) {
    console.log(err);
    res.json({ error: 1 });
  }
};

exports.getMyChallengeId = async (req, res, next) => {
  const userData = req.userToken.id;
  try {
    const myChallenge = await UserChallenge.find({ userId: userData }).populate(
      "userId",
      "nickname"
    );
    res.json({ myChallenge });
  } catch (err) {
    console.log(err);
    res.json({ error: 1 });
  }
};
