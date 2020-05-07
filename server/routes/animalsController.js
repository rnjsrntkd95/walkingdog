const User = require('../models/user');
const Animal = require('../models/animal');
const Breed = require('../models/breed');


// 유저에게 새로운 동물 등록
exports.register = async (req, res, next) => {
    req.userId = '5eb1688e4ab2c850a0f345de';
    req.breed = '푸들';
    req.animalName = '까까';
    req.birth = '2010-01-11';
    req.weight = 10.3;
    req.gender = '남';

    const breed = req.breed;
    const animalName = req.animalName;
    const birth = req.birth;
    const weight = req.weight;
    const gender = req.gender;
    const user = req.userId;

    try {
        const breedId = await Breed.findOne({ breed });
        console.log(breedId)
        const userId = await User.findOne({ _id: user });

        const animalRegister = await new Animal({
            animalName,
            breed: breedId,
            birth,
            weight,
            gender,
            user: userId,
        });
        const resultReg = await Animal.create(animalRegister);
        console.log(resultReg);
        res.json({ successFlag: 1, result: resultReg});
    } catch (err) {
        console.log(`Error: ${err}`);
        res.json({ successFlag: 0, err });
    }
}

// 유저에게 등록된 동물 List
exports.searchPet = async (req, res, next) => {
    req.userId = '5eb1688e4ab2c850a0f345de';
    
    const userId = req.userId;

    try {
        const user = await User.findOne({ _id: userId });
        const myPetList = await Animal.find({ user });

        res.json(myPetList);
    } catch (err) {
        console.log(err)
    }
}

