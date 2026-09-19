module Main where

import           Control.Exception      (evaluate)
import           Control.Monad.Identity
import           Control.Monad.IO.Class

class Symantics repr where
  int :: Int -> repr Int
  bool :: Bool -> repr Bool
  lam :: (repr a -> repr b) -> repr (a -> b)
  app :: repr (a -> b) -> repr a -> repr b

data R x = R x deriving Show

instance Symantics R where
  int = R
  bool = R
  lam f = R (\a ->
      case f (R a) of
        R b -> b
    )
  app (R f) (R x) = R (f x)

data LogR repr a = LogR (IdentityT IO (repr a))

instance (Symantics repr) => Symantics (LogR repr) where
  int n = LogR (runIdentityT (liftIO (putStrLn "Making an int") >> return (int @repr n)))
  bool b = LogR (runIdentityT (liftIO (putStrLn "Making a bool") >> return (bool @repr b)))
  -- app and lam left as exercises for the reader

instance Symantics Maybe where
  int = return

test1 :: Symantics repr => repr Bool
test1 = app (lam id) (bool True)

main :: IO ()
main = do
  -- print @(R Bool) test1
  -- print @(R Int) (app (lam id) (int 42))
  case int @(LogR Maybe) 42 of
    LogR (IdentityT x) -> x
  return ()
