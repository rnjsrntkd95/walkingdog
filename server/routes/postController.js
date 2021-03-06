const User = require("../models/user");
const Animal = require("../models/animal");
const Walking = require("../models/walking");
const Post = require("../models/post");

// create: 새로운 게시글 등록
exports.createPost = async (req, res, next) => {
    const userData = req.userToken.id;
    const requestFiles = req.files
    const content = req.body.comment;
    const walkingId = req.headers.walkingid;
    let image = []
 

    // 새로운 POST의 Image 처리 
    if (requestFiles == null || requestFiles.length == 0) {
        image.push('uploads\\default.jpg')
    } else {
        for (file of requestFiles) {
            image.push(file.path.replace('public\\', ''))
        }
    }
    
    try {
        const user = await User.findOne({ _id: userData });
        const walking = await Walking.findOne({ _id: walkingId });

        const postRegister = new Post({
            content,
            image,
            calorie: walking.calories,
            distance: walking.distance,
            walkingId: walking._id,
            walkingTime: walking.walkingTime,
            addressAdmin: walking.addressAdmin,
            addressLocality: walking.addressLocality,
            addressThoroughfare: walking.addressThoroughfare,
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
};

// 유저의 타임라인 조회
exports.timeline = async (req, res, next) => {
    const addressAdmin = req.query.addressAdmin;
    const addressLocality = req.query.addressLocality;
    const addressThoroughfare = req.query.addressThoroughfare;
    const searchType = req.query.searchType;

    try {
        if (addressAdmin && addressLocality && searchType) {
            if (type == 1) {
                // 지역별
                const posts = await Post.find({ addressAdmin: {$regex: addressAdmin}, 
                    addressLocality: {$regex:addressLocality} }).populate('user', 'profileImage');
                
                // 검색된 포스트가 없으면 포스트 전체를 반환
                // if (posts.length == 0) {
                //     posts = await posts.find({ }).populate('user', 'profileImage');
                // }
    
                res.json({ posts })
            } else if (type == 2) {
                // 견종별


            }

        } else {
            // 위치 정보가 없거나 검색 타입이 없을 때
            const posts = await Post.find({ }).populate('user', 'profileImage').sort("-created_date");
            console.log(posts)
            res.json({ posts, error: 0 })
        }
    } catch (err) {
        console.log(err);
        res.json({ error: 1 });
    }
}

exports.deletePost = async (req, res, next) => {
    const userToken = req.userToken.id;
    const postId = req.body.postId;    

    try {
        const user =  await User.findOne({ _id: userToken });
        if (!user) {
            res.json({ error: -1 });
        } else {
            const regDelete = await Post.deleteOne({ _id: postId, user: user._id });
            console.log(regDelete);
            res.json({});
        }

    } catch (err) {
        console.log(err);
        res.json({ error: 1 });
    };
}