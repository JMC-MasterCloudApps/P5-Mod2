export const secret = "secret";

import {User} from "./models/user.js";
import {Role} from "./models/role.js";
import jsonwebtoken from 'jsonwebtoken';
const {verify} = jsonwebtoken;

const UNAUTHORIZED_CODE = 401;
const FORBIDDEN_CODE = 403;

const NO_TOKEN_RESPONSE = { message: "No token provided!" };
const UNAUTHORIZED_RESPONSE = { message: "Unauthorized!" };

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
    User.findById(req.userId).exec((err, user) => {
        if (err) {
            res.status(500).send({ message: err });
            return;
        }

        Role.find(
            {
                _id: { $in: user.roles }
            },
            (err, roles) => {
                if (err) {
                    res.status(500).send({ message: err });
                    return;
                }

                for (let i = 0; i < roles.length; i++) {
                    if (roles[i].name === "admin") {
                        next();
                        return;
                    }
                }

                res.status(403).send({ message: "Require Admin Role!" });
                return;
            }
        );
    });
};

const authJwt = {
    verifyToken,
    isAdmin
};
export default authJwt;