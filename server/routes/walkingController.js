const User = require('../models/user');
const Animal = require('../models/animal');
const Walking = require('../models/walking');

exports.createWalking = async (req, res, next) => {
    // const userData = req.userToken.id;
    const calorie = 0;
    const distance = 0;
    const routeImage = "";
    const walkingTime = 0;
    const walkingAmount = 0;
    const location = "인천광역시";
    const animalList = ["까까", "브라우니"];
    
    const userData = "5eba8b5ca76e3e20f4b0659e";
    console.log("createWalking 진입");
    try {
        const user = await User.findOne({ _id: userData });
        console.log(user)
        const animal = await Animal.find({animalName: {$in: (animalList)}, user});
        console.log(animal);
        const walking = new Walking();
        const walkingRegister = await new Walking({
            calorie,
            distance,
            routeImage,
            walkingTime,
            walkingAmount,
            location,
            animal,
            user,
        });
        const resultReg = await Walking.create(walkingRegister);
        res.json({});
    } catch (err) {
        console.log(err);
        res.json({ error: 1 });
    }

}