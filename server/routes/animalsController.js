const User = require('../models/user');
const Animal = require('../models/animal');
const Breed = require('../models/breed');

// 모든 견종 조회
exports.checkBreedList = async (req, res, next) => {
    try {
        const breedList = await Breed.find().select('breed');
        console.log(breedList);
        res.json({breedList})
    } catch (err) {
        console.log(err);
    }
}

// 유저에게 새로운 동물 등록
exports.register = async (req, res, next) => {
    const userData = req.userToken.id;
    const breed = req.body.breed;
    const animalName = req.body.animalName;
    const age = req.body.age;
    const weight = req.body.weight;
    const gender = req.body.gender;

    try {
        const userId = await User.findOne({ _id: userData });

        const animalRegister = await new Animal({
            animalName,
            breed,
            age,
            weight,
            gender,
            user: userId,
        });
        const resultReg = await Animal.create(animalRegister);
        console.log(resultReg);
        res.json({});
    } catch (err) {
        console.log(err);

        res.json({error: 1});
    }
}

// 유저에게 등록된 동물 List
exports.searchPet = async (req, res, next) => {
    const userData = req.userToken.id;
    const userId = req.userId;

    try {
        const user = await User.findOne({ _id: userId });
        const myPetList = await Animal.find({ user });

        res.json({ myPetList });
    } catch (err) {
        console.log(err);
        res.json({ error: 1 });
    }
}

