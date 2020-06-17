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

    const newChallengeReg = await Challenge.create(newChallenge);

    const connection = await new UserChallenge({
      userId: producer._id,
      challengeId: newChallengeReg._id,
    });

    const resultReg = await UserChallenge.create(connection);

    const newRecord = await Record({
      user: producer,
      challenge: newChallenge,
      challengeTitle: newChallengeReg.title,
    });
    const resultNewRecord = await Record.create(newRecord);

    res.json({ challengeId: newChallengeReg._id });
  } catch (err) {
    console.log(err);

    res.json({ error: 1 });
  }
};

exports.joinChallenge = async (req, res, next) => {
  const userData = req.userToken.id;
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

      res.json({ });
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
    const challenges = await Challenge.find({}).sort("-create_date");
    // 인기 챌린지
    const popularChallenge = await Challenge.findOne({}).sort("-population");
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
  const challengeId = req.body._id;

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
    const user  = await User.findOne({ _id: userData });

    const records = await Record.find({ challenge: challengeId }).populate("user", "nickname").sort("-score");
    const myRecord = await Record.findOne({ user: userData, challenge: challengeId }).populate("user", "nickname");

    res.json({ records, myRecord });
  } catch (err) {
    console.log(err);
    res.json({ error: 1 });
  }
};

exports.getMyChallengeId = async (req, res, next) => {
  const userData = req.userToken.id;
  try {
    const myChallenge = await UserChallenge.findOne({ userId: userData });
    res.json({ myChallenge: myChallenge.challengeId });
  } catch (err) {
    console.log(err);
    res.json({ error: 1 });
  }
};
