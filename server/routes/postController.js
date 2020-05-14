const User = require('../models/user');
const Animal = require('../models/animal');
const Walking = require('../models/walking');
const Post = require('../models/post');

exports.createPost = async (req, res, next) => {
    // const userData = req.userToken.id;
    const content = "내용1";
    const image = "";
    const walkingId = "5ebd2e5aea2024463cce7a16";
    
    try {
        const walking = await Walking.findOne({ _id: walkingId });
        const postRegister = new Post({
            content,
            image,
            walking,
        });

        const resultReg = await Post.create(postRegister);
        console.log(resultReg);

    } catch (err) {
        console.log(err);
    }


}