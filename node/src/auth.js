export const secret = "secret";

import {User} from "./models/user.js";
import {Role} from "./models/role.js";
import jsonwebtoken from 'jsonwebtoken';
const {verify} = jsonwebtoken;

const UNAUTHORIZED_CODE = 401;
const FORBIDDEN_CODE = 403;
const INTERNAL_ERROR_CODE = 500;

const NO_TOKEN_RESPONSE = { message: "No token provided!" };
const UNAUTHORIZED_RESPONSE = { message: "Unauthorized!" };
const ADMIN_REQUIRED = { message: "Admin Role required!" };

let verifyToken = (req, res, next) => {

    let token = req.headers["x-access-token"];
    if (!token) return res.status(FORBIDDEN_CODE).send(NO_TOKEN_RESPONSE);

    verify (token, secret, (err, decoded) => {
        if (err) return res.status(UNAUTHORIZED_CODE).send(UNAUTHORIZED_RESPONSE);

        req.userId = decoded.id;
        next();
    });
};

let isAdmin = (req, res, next) => {

    User.findById(req.userId)
        .exec((err, user) => {
        if (err) {
            res.status(INTERNAL_ERROR_CODE).send({ message: err });
            return;
        }

        Role.find({
                _id: { $in: user.roles }
            },
            (err, roles) => {
                if (err) {
                    res.status(INTERNAL_ERROR_CODE).send({ message: err });
                    return;
                }

                for (let role of roles) {
                    if (role.name === "admin") {
                        next();
                        return;
                    }
                }

                res.status(FORBIDDEN_CODE).send(ADMIN_REQUIRED);
            }
        );
    });
};

const authJwt = {
    verifyToken,
    isAdmin
};
export default authJwt;