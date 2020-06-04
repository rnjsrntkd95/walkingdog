const User = require("../models/user");
const Animal = require("../models/animal");
const Walking = require("../models/walking");
const Post = require("../models/post");

// create: 새로운 게시글 등록
exports.createPost = async (req, res, next) => {
  // const userData = req.userToken.id;
  const content = "내용3";
  const requestFiles = req.files;
  const walkingId = "5ec534591f2f934d780d1385";
  const userData = "5ebac6bd59e3d32080d6d941";
  let image = [];

  // 새로운 POST의 Image 처리
  if (requestFiles.length == 0) {
    image.push("uploads\\default.jpg");
  } else {
    for (file of requestFiles) {
      image.push(file.path.replace("public\\", ""));
    }
  }

  try {
    const user = await User.findOne({ _id: userData });
    const walking = await Walking.findOne({ _id: walkingId });

    const postRegister = new Post({
      content,
      image,
      calorie: walking.calorie,
      distance: walking.distance,
      routeImage: walking.routeImage,
      walkingTime: walking.walkingTime,
      addressAdmin: walking.addressAdmin,
      addressLocality: walking.addressLocality,
      addressThoroughfare: walking.addressThoroughfare,
      animal: walking.animal,
      // test
      // calorie: 20,
      // distance: 30,
      // walkingTime: 10,
      // animal: ["walnut"],
      user,
      nickname: user.nickname,
    });
    const resultReg = await Post.create(postRegister);
    console.log(resultReg);

    res.json({});
  } catch (err) {
    console.log(err);
    res.json({ error: 1 });
  }
};

// 유저의 타임라인 조회
exports.timeline = async (req, res, next) => {
  // const userData = req.userToken.id;
  const addressAdmin = req.body.addressAdmin;
  const addressLocality = req.body.addressLocality;
  const addressThoroughfare = req.body.addressThoroughfare;

  try {
    if (addressAdmin && addressLocality) {
      const posts = await Post.find({ addressAdmin, addressLocality }).populate(
        "user",
        "profileImage"
      );

      // 검색된 포스트가 없으면 포스트 전체를 반환
      if (posts.length == 0) {
        posts = await posts.find({}).populate("user", "profileImage");
      }

      res.json({ posts });
    } else {
      const posts = await Post.find({}).populate("user", "profileImage");
      console.log(posts);
      res.json({ posts });
    }
  } catch (err) {
    console.log(err);
    res.json({ error: 1 });
  }
};
