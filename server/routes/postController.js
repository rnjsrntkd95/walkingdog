const User = require('../models/user');
const Animal = require('../models/animal');
const Walking = require('../models/walking');
const Post = require('../models/post');


// create: 새로운 게시글 등록
exports.createPost = async (req, res, next) => {
    // const userData = req.userToken.id;
    const content = "내용1";
    const image = "";
    const walkingId = "5ec227624ccf5740cce04cbe";
    const userData = '5eba8b5ca76e3e20f4b0659e';
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
            location: walking.location,
            animal: walking.animal,
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
}

// 유저의 타임라인 조회
exports.timeline = async (req, res, next) => {
    // const userData = req.userToken.id;
    const location = "인천광역시";

    try {
        const posts = await Post.find({ location });

        res.json({ posts })
    } catch (err) {
        console.log(err);
        res.json({ error: 1 });
    }
}