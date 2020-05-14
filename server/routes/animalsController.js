const User = require('../models/user');
const Animal = require('../models/animal');
const Breed = require('../models/breed');

// 모든 견종 조회
exports.checkBreedList = async (req, res, next) => {
    // const List = ["푸들", "말티즈", "리트리버"];
    // for (breed of List) {
    //     Breed.create({breed});
    // }
    try {

        const breedList = await Breed.find().select('breed');
        console.log(breedList)
        res.json({breedList})
        // res.json(breedList.toJSON())
    } catch (err) {
        console.log(err);
    }
}

// 유저에게 새로운 동물 등록
exports.register = async (req, res, next) => {
    req.breed = '푸들';
    req.animalName = '까까';
    req.age = 10;
    req.weight = 10.3;
    req.gender = '남';

    // const userData = req.userToken;
    const userData = "5eba848a4d106033289aeafa";
    const breed = req.breed;
    const animalName = req.animalName;
    const age = req.age;
    const weight = req.weight;
    const gender = req.gender;
    console.log(breed);
    try {
        const breedId = await Breed.findOne({ breed });
        console.log(breedId)
        const userId = await User.findOne({ _id: userData });

        const animalRegister = await new Animal({
            animalName,
            breed: breedId,
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
        // Validation 검사 해서 올바르지 않은 item 반환
        res.json({error: 1});
    }
}

// 유저에게 등록된 동물 List
exports.searchPet = async (req, res, next) => {
    req.userId = '5eb1688e4ab2c850a0f345de';
    
    const userId = req.params.userId;

    try {
        const user = await User.findOne({ _id: userId });
        const myPetList = await Animal.find({ user });

        res.json(myPetList);
    } catch (err) {
        console.log(err);
        console.log({ error: 1 });
    }
}

